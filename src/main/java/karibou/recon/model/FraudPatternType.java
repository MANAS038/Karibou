package karibou.recon.model;

/**
 * Types of fraud patterns detected by the system.
 */
public enum FraudPatternType {
    DRIVER_REPEATED_SHORTFALL,
    AGENT_HIGH_ORPHAN_RATE,
    REFERENCE_CODE_REUSE,
    DRIVER_DEPOSIT_TIMING_ANOMALY,
    AMOUNT_ROUND_DOWN_PATTERN,
    BATCH_INFLATION,
    AGENT_AMOUNT_SKIMMING
}
