package karibou.recon.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * A detected reconciliation issue with context and recommended action.
 */
public class Discrepancy {
    private String discrepancyId;
    private String reconciliationId;
    private DiscrepancyType discrepancyType;
    private Severity severity;
    private String description;
    private List<String> affectedOrderIds;
    private List<String> affectedCollectionIds;
    private String affectedDepositId;
    private String affectedDriverId;
    private String affectedAgentId;
    private Double amountExpected;
    private Double amountActual;
    private Double amountGap;
    private String suggestedAction;
    private boolean autoResolvable;
    private Instant detectedAt;
    private Instant resolvedAt;
    private String resolvedBy;
    private String resolutionNotes;

    public Discrepancy() {
        this.affectedOrderIds = new ArrayList<>();
        this.affectedCollectionIds = new ArrayList<>();
        this.detectedAt = Instant.now();
    }

    // Getters and Setters
    public String getDiscrepancyId() {
        return discrepancyId;
    }

    public void setDiscrepancyId(String discrepancyId) {
        this.discrepancyId = discrepancyId;
    }

    public String getReconciliationId() {
        return reconciliationId;
    }

    public void setReconciliationId(String reconciliationId) {
        this.reconciliationId = reconciliationId;
    }

    public DiscrepancyType getDiscrepancyType() {
        return discrepancyType;
    }

    public void setDiscrepancyType(DiscrepancyType discrepancyType) {
        this.discrepancyType = discrepancyType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAffectedOrderIds() {
        return affectedOrderIds;
    }

    public void setAffectedOrderIds(List<String> affectedOrderIds) {
        this.affectedOrderIds = affectedOrderIds;
    }

    public List<String> getAffectedCollectionIds() {
        return affectedCollectionIds;
    }

    public void setAffectedCollectionIds(List<String> affectedCollectionIds) {
        this.affectedCollectionIds = affectedCollectionIds;
    }

    public String getAffectedDepositId() {
        return affectedDepositId;
    }

    public void setAffectedDepositId(String affectedDepositId) {
        this.affectedDepositId = affectedDepositId;
    }

    public String getAffectedDriverId() {
        return affectedDriverId;
    }

    public void setAffectedDriverId(String affectedDriverId) {
        this.affectedDriverId = affectedDriverId;
    }

    public String getAffectedAgentId() {
        return affectedAgentId;
    }

    public void setAffectedAgentId(String affectedAgentId) {
        this.affectedAgentId = affectedAgentId;
    }

    public Double getAmountExpected() {
        return amountExpected;
    }

    public void setAmountExpected(Double amountExpected) {
        this.amountExpected = amountExpected;
    }

    public Double getAmountActual() {
        return amountActual;
    }

    public void setAmountActual(Double amountActual) {
        this.amountActual = amountActual;
    }

    public Double getAmountGap() {
        return amountGap;
    }

    public void setAmountGap(Double amountGap) {
        this.amountGap = amountGap;
    }

    public String getSuggestedAction() {
        return suggestedAction;
    }

    public void setSuggestedAction(String suggestedAction) {
        this.suggestedAction = suggestedAction;
    }

    public boolean isAutoResolvable() {
        return autoResolvable;
    }

    public void setAutoResolvable(boolean autoResolvable) {
        this.autoResolvable = autoResolvable;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Instant resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    @Override
    public String toString() {
        return "Discrepancy{" +
                "id='" + discrepancyId + '\'' +
                ", type=" + discrepancyType +
                ", severity=" + severity +
                ", gap=" + amountGap +
                '}';
    }
}
