package karibou.recon.dto;

import java.time.Instant;

/**
 * Request to trigger a reconciliation run.
 */
public class RunRequest {
    private Instant dateRangeStart;
    private Instant dateRangeEnd;
    private String region;  // "Kenya", "Uganda", "Tanzania", "ALL", or null
    private Double autoReconcileThreshold;
    private Integer missingDepositThresholdHours;

    public RunRequest() {
    }

    public RunRequest(Instant dateRangeStart, Instant dateRangeEnd) {
        this.dateRangeStart = dateRangeStart;
        this.dateRangeEnd = dateRangeEnd;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Double getAutoReconcileThreshold() {
        return autoReconcileThreshold;
    }

    public void setAutoReconcileThreshold(Double autoReconcileThreshold) {
        this.autoReconcileThreshold = autoReconcileThreshold;
    }

    public Integer getMissingDepositThresholdHours() {
        return missingDepositThresholdHours;
    }

    public void setMissingDepositThresholdHours(Integer missingDepositThresholdHours) {
        this.missingDepositThresholdHours = missingDepositThresholdHours;
    }
}
