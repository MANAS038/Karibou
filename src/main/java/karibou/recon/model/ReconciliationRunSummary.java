package karibou.recon.model;

/**
 * Summary statistics for a reconciliation run.
 */
public class ReconciliationRunSummary {
    private int totalOrdersProcessed;
    private int totalCollectionsProcessed;
    private int totalDepositsProcessed;
    private int exactMatches;
    private int probableMatches;
    private int possibleMatches;
    private int unmatched;
    private int autoReconciled;
    private int pendingReview;
    private double totalExpectedAmount;
    private double totalDepositedAmount;
    private double totalVariance;
    private double totalMissingCash;
    private Currency currency;
    private int discrepanciesRaised;
    private int fraudPatternsDetected;

    public ReconciliationRunSummary() {
    }

    // Getters and Setters
    public int getTotalOrdersProcessed() {
        return totalOrdersProcessed;
    }

    public void setTotalOrdersProcessed(int totalOrdersProcessed) {
        this.totalOrdersProcessed = totalOrdersProcessed;
    }

    public int getTotalCollectionsProcessed() {
        return totalCollectionsProcessed;
    }

    public void setTotalCollectionsProcessed(int totalCollectionsProcessed) {
        this.totalCollectionsProcessed = totalCollectionsProcessed;
    }

    public int getTotalDepositsProcessed() {
        return totalDepositsProcessed;
    }

    public void setTotalDepositsProcessed(int totalDepositsProcessed) {
        this.totalDepositsProcessed = totalDepositsProcessed;
    }

    public int getExactMatches() {
        return exactMatches;
    }

    public void setExactMatches(int exactMatches) {
        this.exactMatches = exactMatches;
    }

    public int getProbableMatches() {
        return probableMatches;
    }

    public void setProbableMatches(int probableMatches) {
        this.probableMatches = probableMatches;
    }

    public int getPossibleMatches() {
        return possibleMatches;
    }

    public void setPossibleMatches(int possibleMatches) {
        this.possibleMatches = possibleMatches;
    }

    public int getUnmatched() {
        return unmatched;
    }

    public void setUnmatched(int unmatched) {
        this.unmatched = unmatched;
    }

    public int getAutoReconciled() {
        return autoReconciled;
    }

    public void setAutoReconciled(int autoReconciled) {
        this.autoReconciled = autoReconciled;
    }

    public int getPendingReview() {
        return pendingReview;
    }

    public void setPendingReview(int pendingReview) {
        this.pendingReview = pendingReview;
    }

    public double getTotalExpectedAmount() {
        return totalExpectedAmount;
    }

    public void setTotalExpectedAmount(double totalExpectedAmount) {
        this.totalExpectedAmount = totalExpectedAmount;
    }

    public double getTotalDepositedAmount() {
        return totalDepositedAmount;
    }

    public void setTotalDepositedAmount(double totalDepositedAmount) {
        this.totalDepositedAmount = totalDepositedAmount;
    }

    public double getTotalVariance() {
        return totalVariance;
    }

    public void setTotalVariance(double totalVariance) {
        this.totalVariance = totalVariance;
    }

    public double getTotalMissingCash() {
        return totalMissingCash;
    }

    public void setTotalMissingCash(double totalMissingCash) {
        this.totalMissingCash = totalMissingCash;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getDiscrepanciesRaised() {
        return discrepanciesRaised;
    }

    public void setDiscrepanciesRaised(int discrepanciesRaised) {
        this.discrepanciesRaised = discrepanciesRaised;
    }

    public int getFraudPatternsDetected() {
        return fraudPatternsDetected;
    }

    public void setFraudPatternsDetected(int fraudPatternsDetected) {
        this.fraudPatternsDetected = fraudPatternsDetected;
    }
}
