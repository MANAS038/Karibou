package karibou.recon.model;

import java.time.Instant;

/**
 * Metadata for a full reconciliation batch run.
 */
public class ReconciliationRun {
    private String runId;
    private String triggeredBy;  // "scheduler", "manual", "api"
    private Instant dateRangeStart;
    private Instant dateRangeEnd;
    private String regionFilter;  // Optional: "Kenya", "Uganda", "Tanzania", or null for all
    private RunStatus status;
    private Instant startedAt;
    private Instant completedAt;
    private ReconciliationRunSummary summary;

    public ReconciliationRun() {
        this.startedAt = Instant.now();
        this.status = RunStatus.RUNNING;
    }

    public ReconciliationRun(String runId, Instant dateRangeStart, Instant dateRangeEnd) {
        this();
        this.runId = runId;
        this.dateRangeStart = dateRangeStart;
        this.dateRangeEnd = dateRangeEnd;
    }

    // Getters and Setters
    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public Instant getDateRangeStart() {
        return dateRangeStart;
    }

    public void setDateRangeStart(Instant dateRangeStart) {
        this.dateRangeStart = dateRangeStart;
    }

    public Instant getDateRangeEnd() {
        return dateRangeEnd;
    }

    public void setDateRangeEnd(Instant dateRangeEnd) {
        this.dateRangeEnd = dateRangeEnd;
    }

    public String getRegionFilter() {
        return regionFilter;
    }

    public void setRegionFilter(String regionFilter) {
        this.regionFilter = regionFilter;
    }

    public RunStatus getStatus() {
        return status;
    }

    public void setStatus(RunStatus status) {
        this.status = status;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }

    public ReconciliationRunSummary getSummary() {
        return summary;
    }

    public void setSummary(ReconciliationRunSummary summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "ReconciliationRun{" +
                "runId='" + runId + '\'' +
                ", status=" + status +
                ", startedAt=" + startedAt +
                ", completedAt=" + completedAt +
                '}';
    }
}
