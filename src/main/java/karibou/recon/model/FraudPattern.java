package karibou.recon.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Detected suspicious pattern suggesting fraud or systematic error.
 */
public class FraudPattern {
    private String patternId;
    private FraudPatternType patternType;
    private Severity severity;
    private EntityType entityType;
    private String entityId;
    private String description;
    private int transactionCount;
    private double totalAmountAtRisk;
    private Currency currency;
    private Instant dateRangeStart;
    private Instant dateRangeEnd;
    private List<String> supportingDiscrepancyIds;
    private String suggestedAction;
    private Instant detectedAt;

    public FraudPattern() {
        this.supportingDiscrepancyIds = new ArrayList<>();
        this.detectedAt = Instant.now();
    }

    // Getters and Setters
    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public FraudPatternType getPatternType() {
        return patternType;
    }

    public void setPatternType(FraudPatternType patternType) {
        this.patternType = patternType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(int transactionCount) {
        this.transactionCount = transactionCount;
    }

    public double getTotalAmountAtRisk() {
        return totalAmountAtRisk;
    }

    public void setTotalAmountAtRisk(double totalAmountAtRisk) {
        this.totalAmountAtRisk = totalAmountAtRisk;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public List<String> getSupportingDiscrepancyIds() {
        return supportingDiscrepancyIds;
    }

    public void setSupportingDiscrepancyIds(List<String> supportingDiscrepancyIds) {
        this.supportingDiscrepancyIds = supportingDiscrepancyIds;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    @Override
    public String toString() {
        return "FraudPattern{" +
                "id='" + patternId + '\'' +
                ", type=" + patternType +
                ", severity=" + severity +
                ", entity=" + entityType + ":" + entityId +
                ", risk=" + totalAmountAtRisk +
                '}';
    }
}
