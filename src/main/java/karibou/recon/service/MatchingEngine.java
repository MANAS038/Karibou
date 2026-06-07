package karibou.recon.service;

import karibou.recon.config.EngineConfig;
import karibou.recon.model.*;
import karibou.recon.repository.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Core reconciliation matching engine implementing the 10-step pipeline.
 * Matches orders → driver collections → agent deposits using exact and fuzzy matching.
 */
public class MatchingEngine {
    private final OrderRepository orderRepository;
    private final DriverCollectionRepository collectionRepository;
    private final AgentDepositRepository depositRepository;
    private final ReconciliationResultRepository resultRepository;
    private final DiscrepancyRepository discrepancyRepository;
    
    private EngineConfig config;
    private int reconciliationIdCounter = 0;

    public MatchingEngine(OrderRepository orderRepository,
                         DriverCollectionRepository collectionRepository,
                         AgentDepositRepository depositRepository,
                         ReconciliationResultRepository resultRepository,
                         DiscrepancyRepository discrepancyRepository,
                         EngineConfig config) {
        this.orderRepository = orderRepository;
        this.collectionRepository = collectionRepository;
        this.depositRepository = depositRepository;
        this.resultRepository = resultRepository;
        this.discrepancyRepository = discrepancyRepository;
        this.config = config;
    }

    /**
     * Main entry point: run reconciliation for given date range and region.
     * Returns list of ReconciliationResult objects.
     */
    public List<ReconciliationResult> runReconciliation(Instant dateRangeStart, Instant dateRangeEnd, String region) {
        List<ReconciliationResult> results = new ArrayList<>();
        
        // Step 1: Fetch data for the date range
        List<Order> orders = orderRepository.findByDateRange(dateRangeStart, dateRangeEnd);
        List<DriverCollection> collections = collectionRepository.findByDateRange(dateRangeStart, dateRangeEnd);
        List<AgentDeposit> deposits = depositRepository.findByDateRange(dateRangeStart, dateRangeEnd);

        // Filter by region if specified
        if (region != null && !region.equalsIgnoreCase("ALL")) {
            orders = orders.stream()
                    .filter(o -> region.equalsIgnoreCase(o.getRegion()))
                    .collect(Collectors.toList());
        }

        // Track which deposits have been matched
        Set<String> matchedDepositIds = new HashSet<>();
        
        // Step 2-7: Attempt exact and fuzzy matching for each collection
        for (DriverCollection collection : collections) {
            ReconciliationResult result = matchCollectionToDeposit(collection, deposits, orders, matchedDepositIds);
            if (result != null) {
                results.add(result);
                resultRepository.save(result);
            }
        }

        // Handle orphaned deposits (deposits with no match)
        for (AgentDeposit deposit : deposits) {
            if (!matchedDepositIds.contains(deposit.getDepositId())) {
                ReconciliationResult orphan = createOrphanedDepositResult(deposit);
                results.add(orphan);
                resultRepository.save(orphan);
            }
        }

        return results;
    }

    /**
     * Step 2-7: Match a collection to a deposit using scoring algorithm.
     */
    private ReconciliationResult matchCollectionToDeposit(DriverCollection collection, 
                                                         List<AgentDeposit> allDeposits,
                                                         List<Order> allOrders,
                                                         Set<String> matchedDepositIds) {
        // Find candidate deposits
        List<AgentDeposit> candidates = findCandidateDeposits(collection, allDeposits, matchedDepositIds);
        
        if (candidates.isEmpty()) {
            return createMissingDepositResult(collection, allOrders);
        }

        // Score each candidate
        AgentDeposit bestMatch = null;
        double bestScore = 0.0;
        ScoringBreakdown bestBreakdown = null;

        for (AgentDeposit deposit : candidates) {
            ScoringBreakdown breakdown = computeScore(collection, deposit);
            if (breakdown.getWeightedTotal() > bestScore) {
                bestScore = breakdown.getWeightedTotal();
                bestMatch = deposit;
                bestBreakdown = breakdown;
            }
        }

        if (bestMatch == null || bestScore < 0.5) {
            return createMissingDepositResult(collection, allOrders);
        }

        // Mark deposit as matched
        matchedDepositIds.add(bestMatch.getDepositId());

        // Create reconciliation result
        return createReconciliationResult(collection, bestMatch, allOrders, bestBreakdown);
    }

    /**
     * Find candidate deposits for a collection based on driver, timing, and amount.
     */
    private List<AgentDeposit> findCandidateDeposits(DriverCollection collection, 
                                                     List<AgentDeposit> allDeposits,
                                                     Set<String> matchedDepositIds) {
        return allDeposits.stream()
                .filter(d -> !matchedDepositIds.contains(d.getDepositId()))
                .filter(d -> {
                    // Must be same driver (if driver ID is captured)
                    if (d.getDriverId() != null && !d.getDriverId().equals(collection.getDriverId())) {
                        return false;
                    }
                    // Deposit must be after or near collection time
                    long hoursDiff = Duration.between(collection.getCollectionTimestamp(), d.getDepositTimestamp()).toHours();
                    if (hoursDiff < -2 || hoursDiff > config.getTimingWindowCollectionToDepositMaxHours()) {
                        return false;
                    }
                    // Amount must be reasonably close
                    double amountDiff = Math.abs(d.getDepositedAmount() - collection.getCollectedAmount());
                    double tolerance = Math.max(
                            config.getAbsoluteToleranceFor(collection.getCurrency()),
                            collection.getCollectedAmount() * config.getAmountTolerancePct() / 100.0
                    );
                    return amountDiff <= tolerance * 3; // Allow 3x tolerance for candidates
                })
                .collect(Collectors.toList());
    }

    /**
     * Step 3-6: Compute composite score breakdown.
     */
    private ScoringBreakdown computeScore(DriverCollection collection, AgentDeposit deposit) {
        // Compute each component score
        double refScore = computeReferenceCodeScore(collection.getReferenceCode(), deposit.getReferenceCode());
        double amtScore = computeAmountScore(collection.getCollectedAmount(), deposit.getDepositedAmount(), collection.getCurrency());
        double timeScore = computeTimingScore(collection.getCollectionTimestamp(), deposit.getDepositTimestamp());
        double driverScore = computeDriverScore(collection.getDriverId(), deposit.getDriverId());

        return new ScoringBreakdown(refScore, amtScore, timeScore, driverScore);
    }

    /**
     * Step 3: Reference code matching with fuzzy logic.
     */
    private double computeReferenceCodeScore(String collectionRef, String depositRef) {
        if (collectionRef == null || depositRef == null) {
            return 0.0;
        }

        // Exact match
        if (collectionRef.equals(depositRef)) {
            return 1.0;
        }

        // Fuzzy match using Levenshtein distance
        int editDistance = levenshteinDistance(collectionRef, depositRef);
        if (editDistance == 0) return 1.0;
        if (editDistance == 1) return 0.85;
        if (editDistance == 2) return 0.70;

        // Prefix match (first 6 chars)
        if (collectionRef.length() >= 6 && depositRef.length() >= 6) {
            if (collectionRef.substring(0, 6).equals(depositRef.substring(0, 6))) {
                return 0.50;
            }
        }

        return 0.0;
    }

    /**
     * Step 4: Amount matching with tolerance.
     */
    private double computeAmountScore(double collected, double deposited, karibou.recon.model.Currency currency) {
        double diff = Math.abs(collected - deposited);
        double pctDiff = (diff / collected) * 100.0;

        if (diff == 0) return 1.0;
        if (pctDiff <= 1.0) return 0.90;
        if (pctDiff <= 2.0) return 0.75;
        if (pctDiff <= 5.0) return 0.50;
        return 0.0;
    }

    /**
     * Step 5: Timing validation.
     */
    private double computeTimingScore(Instant collectionTime, Instant depositTime) {
        long hoursDiff = Duration.between(collectionTime, depositTime).toHours();

        // Invalid sequence (deposit before collection)
        if (hoursDiff < 0) return 0.0;

        // Valid within 24h
        if (hoursDiff <= 24) return 1.0;
        // Valid within 48h
        if (hoursDiff <= 48) return 0.70;
        // Valid but > 48h
        return 0.30;
    }

    /**
     * Step 6: Driver ID correlation.
     */
    private double computeDriverScore(String collectionDriverId, String depositDriverId) {
        if (collectionDriverId == null || depositDriverId == null) {
            return 0.5; // Neutral if missing
        }
        return collectionDriverId.equals(depositDriverId) ? 1.0 : 0.0;
    }

    /**
     * Levenshtein distance algorithm for fuzzy string matching.
     */
    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private ReconciliationResult createReconciliationResult(DriverCollection collection, AgentDeposit deposit,
                                                           List<Order> allOrders, ScoringBreakdown breakdown) {
        ReconciliationResult result = new ReconciliationResult();
        result.setReconciliationId("REC-" + System.currentTimeMillis() + "-" + (reconciliationIdCounter++));
        result.setMatchType(MatchType.ONE_TO_ONE);
        result.setConfidenceScore(breakdown.getWeightedTotal());
        result.setScoringBreakdown(breakdown);

        // Determine match quality
        if (breakdown.getWeightedTotal() >= 0.95) {
            result.setMatchQuality(MatchQuality.EXACT_MATCH);
            result.setStatus(ReconciliationStatus.AUTO_RECONCILED);
        } else if (breakdown.getWeightedTotal() >= 0.75) {
            result.setMatchQuality(MatchQuality.PROBABLE_MATCH);
            result.setStatus(ReconciliationStatus.AUTO_RECONCILED);
        } else if (breakdown.getWeightedTotal() >= 0.50) {
            result.setMatchQuality(MatchQuality.POSSIBLE_MATCH);
            result.setStatus(ReconciliationStatus.PENDING_REVIEW);
        } else {
            result.setMatchQuality(MatchQuality.UNMATCHED);
            result.setStatus(ReconciliationStatus.PENDING_REVIEW);
        }

        result.setCollectionIds(Arrays.asList(collection.getCollectionId()));
        result.setDepositId(deposit.getDepositId());
        result.setOrderIds(new ArrayList<>(collection.getOrderIds()));

        // Calculate amounts
        result.setTotalCollectedAmount(collection.getCollectedAmount());
        result.setTotalDepositedAmount(deposit.getDepositedAmount());
        result.setCurrency(collection.getCurrency());

        double variance = collection.getCollectedAmount() - deposit.getDepositedAmount();
        result.setAmountVariance(Math.abs(variance));
        result.setAmountVariancePct((Math.abs(variance) / collection.getCollectedAmount()) * 100.0);

        return result;
    }

    private ReconciliationResult createMissingDepositResult(DriverCollection collection, List<Order> allOrders) {
        ReconciliationResult result = new ReconciliationResult();
        result.setReconciliationId("REC-" + System.currentTimeMillis() + "-" + (reconciliationIdCounter++));
        result.setMatchQuality(MatchQuality.UNMATCHED);
        result.setStatus(ReconciliationStatus.PENDING_REVIEW);
        result.setConfidenceScore(0.0);
        result.setCollectionIds(Arrays.asList(collection.getCollectionId()));
        result.setOrderIds(new ArrayList<>(collection.getOrderIds()));
        result.setTotalCollectedAmount(collection.getCollectedAmount());
        result.setCurrency(collection.getCurrency());
        return result;
    }

    private ReconciliationResult createOrphanedDepositResult(AgentDeposit deposit) {
        ReconciliationResult result = new ReconciliationResult();
        result.setReconciliationId("REC-" + System.currentTimeMillis() + "-" + (reconciliationIdCounter++));
        result.setMatchQuality(MatchQuality.UNMATCHED);
        result.setStatus(ReconciliationStatus.PENDING_REVIEW);
        result.setConfidenceScore(0.0);
        result.setDepositId(deposit.getDepositId());
        result.setTotalDepositedAmount(deposit.getDepositedAmount());
        result.setCurrency(deposit.getCurrency());
        return result;
    }
}
