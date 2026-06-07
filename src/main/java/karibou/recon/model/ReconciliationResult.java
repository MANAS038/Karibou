package karibou.recon.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Output of the matching engine for a single matched group.
 */
public class ReconciliationResult {
    private String reconciliationId;
    private MatchType matchType;
    private MatchQuality matchQuality;
    private double confidenceScore;
    private List<String> orderIds;
    private List<String> collectionIds;
    private String depositId;
    private double totalExpectedAmount;
    private double totalCollectedAmount;
    private double totalDepositedAmount;
    private Currency currency;
    private double amountVariance;
    private double amountVariancePct;
    private boolean timingValid;
    private Instant matchedAt;
    private ReconciliationStatus status;
    private List<Discrepancy> discrepancies;
    private ScoringBreakdown scoringBreakdown;

    public ReconciliationResult() {
        this.orderIds = new ArrayList<>();
        this.collectionIds = new ArrayList<>();
        this.discrepancies = new ArrayList<>();
        this.matchedAt = Instant.now();
    }

    // Getters and Setters
    public String getReconciliationId() {
        return reconciliationId;
    }

    public void setReconciliationId(String reconciliationId) {
        this.reconciliationId = reconciliationId;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public MatchQuality getMatchQuality() {
        return matchQuality;
    }

    public void setMatchQuality(MatchQuality matchQuality) {
        this.matchQuality = matchQuality;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds;
    }

    public List<String> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(List<String> collectionIds) {
        this.collectionIds = collectionIds;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public double getTotalExpectedAmount() {
        return totalExpectedAmount;
    }

    public void setTotalExpectedAmount(double totalExpectedAmount) {
        this.totalExpectedAmount = totalExpectedAmount;
    }

    public double getTotalCollectedAmount() {
        return totalCollectedAmount;
    }

    public void setTotalCollectedAmount(double totalCollectedAmount) {
        this.totalCollectedAmount = totalCollectedAmount;
    }

    public double getTotalDepositedAmount() {
        return totalDepositedAmount;
    }

    public void setTotalDepositedAmount(double totalDepositedAmount) {
        this.totalDepositedAmount = totalDepositedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getAmountVariance() {
        return amountVariance;
    }

    public void setAmountVariance(double amountVariance) {
        this.amountVariance = amountVariance;
    }

    public double getAmountVariancePct() {
        return amountVariancePct;
    }

    public void setAmountVariancePct(double amountVariancePct) {
        this.amountVariancePct = amountVariancePct;
    }

    public boolean isTimingValid() {
        return timingValid;
    }

    public void setTimingValid(boolean timingValid) {
        this.timingValid = timingValid;
    }

    public Instant getMatchedAt() {
        return matchedAt;
    }

    public void setMatchedAt(Instant matchedAt) {
        this.matchedAt = matchedAt;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationStatus status) {
        this.status = status;
    }

    public List<Discrepancy> getDiscrepancies() {
        return discrepancies;
    }

    public void setDiscrepancies(List<Discrepancy> discrepancies) {
        this.discrepancies = discrepancies;
    }

    public ScoringBreakdown getScoringBreakdown() {
        return scoringBreakdown;
    }

    public void setScoringBreakdown(ScoringBreakdown scoringBreakdown) {
        this.scoringBreakdown = scoringBreakdown;
    }

    @Override
    public String toString() {
        return "ReconciliationResult{" +
                "id='" + reconciliationId + '\'' +
                ", quality=" + matchQuality +
                ", confidence=" + confidenceScore +
                ", status=" + status +
                '}';
    }
}
