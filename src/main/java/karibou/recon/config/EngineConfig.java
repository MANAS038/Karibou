package karibou.recon.config;

import karibou.recon.model.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * Configurable engine parameters for the reconciliation matching engine.
 * All thresholds and weights can be tuned based on business requirements.
 */
public class EngineConfig {
    // Amount matching thresholds
    private double amountTolerancePct = 2.0;  // Max % variance to still consider an amount match
    private Map<Currency, Integer> amountToleranceAbsolute;  // Absolute tolerance per currency

    // Fuzzy reference matching
    private int fuzzyReferenceEditDistanceMax = 2;  // Max Levenshtein edit distance for fuzzy matching

    // Timing windows
    private int timingWindowCollectionToDepositMaxHours = 72;  // Max hours between collection and deposit
    private int timingWindowOrderToCollectionMaxHours = 24;   // Max hours between order and collection

    // Batch detection
    private int batchMaxOrders = 15;  // Max orders to consider in a single batched deposit

    // Auto-reconciliation threshold
    private double autoReconcileConfidenceThreshold = 0.95;  // Min confidence score to auto-reconcile

    // Fraud detection thresholds
    private double fraudDriverShortfallRateThreshold = 0.30;  // Shortfall rate above which driver is flagged
    private double fraudAgentOrphanRateThreshold = 0.20;      // Orphan deposit rate above which agent is flagged
    private int fraudLookbackDays = 30;                       // Rolling window for fraud pattern detection

    // Scoring weights (must sum to 1.0)
    private double weightReferenceCode = 0.35;
    private double weightAmount = 0.35;
    private double weightTiming = 0.20;
    private double weightDriverId = 0.10;

    // Base currency for multi-currency support (SG-3)
    private Currency baseCurrency = Currency.KES;

    public EngineConfig() {
        // Initialize default absolute tolerances per currency
        amountToleranceAbsolute = new HashMap<>();
        amountToleranceAbsolute.put(Currency.KES, 50);
        amountToleranceAbsolute.put(Currency.UGX, 2000);
        amountToleranceAbsolute.put(Currency.TZS, 1500);
    }

    // Getters and Setters
    public double getAmountTolerancePct() {
        return amountTolerancePct;
    }

    public void setAmountTolerancePct(double amountTolerancePct) {
        this.amountTolerancePct = amountTolerancePct;
    }

    public Map<Currency, Integer> getAmountToleranceAbsolute() {
        return amountToleranceAbsolute;
    }

    public void setAmountToleranceAbsolute(Map<Currency, Integer> amountToleranceAbsolute) {
        this.amountToleranceAbsolute = amountToleranceAbsolute;
    }

    public int getAbsoluteToleranceFor(Currency currency) {
        return amountToleranceAbsolute.getOrDefault(currency, 50);
    }

    public int getFuzzyReferenceEditDistanceMax() {
        return fuzzyReferenceEditDistanceMax;
    }

    public void setFuzzyReferenceEditDistanceMax(int fuzzyReferenceEditDistanceMax) {
        this.fuzzyReferenceEditDistanceMax = fuzzyReferenceEditDistanceMax;
    }

    public int getTimingWindowCollectionToDepositMaxHours() {
        return timingWindowCollectionToDepositMaxHours;
    }

    public void setTimingWindowCollectionToDepositMaxHours(int timingWindowCollectionToDepositMaxHours) {
        this.timingWindowCollectionToDepositMaxHours = timingWindowCollectionToDepositMaxHours;
    }

    public int getTimingWindowOrderToCollectionMaxHours() {
        return timingWindowOrderToCollectionMaxHours;
    }

    public void setTimingWindowOrderToCollectionMaxHours(int timingWindowOrderToCollectionMaxHours) {
        this.timingWindowOrderToCollectionMaxHours = timingWindowOrderToCollectionMaxHours;
    }

    public int getBatchMaxOrders() {
        return batchMaxOrders;
    }

    public void setBatchMaxOrders(int batchMaxOrders) {
        this.batchMaxOrders = batchMaxOrders;
    }

    public double getAutoReconcileConfidenceThreshold() {
        return autoReconcileConfidenceThreshold;
    }

    public void setAutoReconcileConfidenceThreshold(double autoReconcileConfidenceThreshold) {
        this.autoReconcileConfidenceThreshold = autoReconcileConfidenceThreshold;
    }

    public double getFraudDriverShortfallRateThreshold() {
        return fraudDriverShortfallRateThreshold;
    }

    public void setFraudDriverShortfallRateThreshold(double fraudDriverShortfallRateThreshold) {
        this.fraudDriverShortfallRateThreshold = fraudDriverShortfallRateThreshold;
    }

    public double getFraudAgentOrphanRateThreshold() {
        return fraudAgentOrphanRateThreshold;
    }

    public void setFraudAgentOrphanRateThreshold(double fraudAgentOrphanRateThreshold) {
        this.fraudAgentOrphanRateThreshold = fraudAgentOrphanRateThreshold;
    }

    public int getFraudLookbackDays() {
        return fraudLookbackDays;
    }

    public void setFraudLookbackDays(int fraudLookbackDays) {
        this.fraudLookbackDays = fraudLookbackDays;
    }

    public double getWeightReferenceCode() {
        return weightReferenceCode;
    }

    public double getWeightAmount() {
        return weightAmount;
    }

    public double getWeightTiming() {
        return weightTiming;
    }

    public double getWeightDriverId() {
        return weightDriverId;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
