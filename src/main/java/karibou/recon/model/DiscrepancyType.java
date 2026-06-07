package karibou.recon.model;

/**
 * Types of discrepancies that can be detected in reconciliation.
 */
public enum DiscrepancyType {
    AMOUNT_MISMATCH,
    MISSING_DEPOSIT,
    ORPHANED_DEPOSIT,
    TIMING_ANOMALY,
    REFERENCE_CODE_MISMATCH,
    DUPLICATE_REFERENCE,
    SUSPICIOUS_DRIVER_PATTERN,
    SUSPICIOUS_AGENT_PATTERN,
    BATCH_SPLIT_MISMATCH,
    CURRENCY_MISMATCH
}
