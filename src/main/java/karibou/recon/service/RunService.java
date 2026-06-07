package karibou.recon.service;

import karibou.recon.config.EngineConfig;
import karibou.recon.dto.RunRequest;
import karibou.recon.model.*;
import karibou.recon.repository.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing reconciliation runs.
 * Orchestrates the matching engine and tracks run metadata.
 */
public class RunService {
    private final ReconciliationRunRepository runRepository;
    private final MatchingEngine matchingEngine;
    private final EngineConfig engineConfig;
    private int runCounter = 0;

    public RunService(ReconciliationRunRepository runRepository,
                     MatchingEngine matchingEngine,
                     EngineConfig engineConfig) {
        this.runRepository = runRepository;
        this.matchingEngine = matchingEngine;
        this.engineConfig = engineConfig;
    }

    /**
     * Start a new reconciliation run.
     */
    public ReconciliationRun startRun(RunRequest request) {
        // Create run metadata
        String runId = generateRunId();
        ReconciliationRun run = new ReconciliationRun(runId, request.getDateRangeStart(), request.getDateRangeEnd());
        run.setTriggeredBy("api");
        run.setRegionFilter(request.getRegion());
        run.setStatus(RunStatus.RUNNING);
        run.setStartedAt(Instant.now());

        // Save run
        runRepository.save(run);

        // Execute matching engine in background (for now, synchronously)
        try {
            List<ReconciliationResult> results = matchingEngine.runReconciliation(
                    request.getDateRangeStart(),
                    request.getDateRangeEnd(),
                    request.getRegion()
            );

            // Build summary
            ReconciliationRunSummary summary = buildSummary(results);
            run.setSummary(summary);
            run.setStatus(RunStatus.COMPLETED);
            run.setCompletedAt(Instant.now());

        } catch (Exception e) {
            run.setStatus(RunStatus.FAILED);
            run.setCompletedAt(Instant.now());
        }

        runRepository.save(run);
        return run;
    }

    /**
     * Get run status and summary by run ID.
     */
    public Optional<ReconciliationRun> getRun(String runId) {
        return runRepository.findById(runId);
    }

    /**
     * List all runs.
     */
    public List<ReconciliationRun> getAllRuns() {
        return runRepository.findAll();
    }

    private String generateRunId() {
        return "RUN-" + System.currentTimeMillis() + "-" + (runCounter++);
    }

    private ReconciliationRunSummary buildSummary(List<ReconciliationResult> results) {
        ReconciliationRunSummary summary = new ReconciliationRunSummary();

        int exactMatches = 0;
        int probableMatches = 0;
        int possibleMatches = 0;
        int unmatched = 0;
        int autoReconciled = 0;
        int pendingReview = 0;

        double totalExpected = 0.0;
        double totalDeposited = 0.0;

        for (ReconciliationResult result : results) {
            // Count by match quality
            switch (result.getMatchQuality()) {
                case EXACT_MATCH: exactMatches++; break;
                case PROBABLE_MATCH: probableMatches++; break;
                case POSSIBLE_MATCH: possibleMatches++; break;
                case UNMATCHED: unmatched++; break;
            }

            // Count by status
            if (result.getStatus() == ReconciliationStatus.AUTO_RECONCILED) {
                autoReconciled++;
            } else if (result.getStatus() == ReconciliationStatus.PENDING_REVIEW) {
                pendingReview++;
            }

            // Sum amounts
            totalDeposited += result.getTotalDepositedAmount();
        }

        summary.setExactMatches(exactMatches);
        summary.setProbableMatches(probableMatches);
        summary.setPossibleMatches(possibleMatches);
        summary.setUnmatched(unmatched);
        summary.setAutoReconciled(autoReconciled);
        summary.setPendingReview(pendingReview);
        summary.setTotalDepositedAmount(totalDeposited);
        summary.setTotalVariance(Math.abs(totalExpected - totalDeposited));

        return summary;
    }
}
