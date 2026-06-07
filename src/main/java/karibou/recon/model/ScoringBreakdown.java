package karibou.recon.model;

/**
 * Breakdown of how confidence score was computed for a reconciliation result.
 */
public class ScoringBreakdown {
    private double referenceCodeScore;  // 1.0=exact match, 0.8=fuzzy (edit ≤2), 0.4=partial, 0.0=missing
    private double amountScore;         // 1.0=exact, 0.9=within 1%, 0.7=within 2%, 0.5=within 5%, 0.0=beyond 5%
    private double timingScore;         // 1.0=valid sequence within 24h, 0.7=within 48h, 0.3=>48h, 0.0=invalid
    private double driverIdScore;       // 1.0=all match, 0.5=2/3 match, 0.0=no match
    private double weightedTotal;       // ref*0.35 + amt*0.35 + time*0.20 + driver*0.10

    public ScoringBreakdown() {
    }

    public ScoringBreakdown(double referenceCodeScore, double amountScore, 
                           double timingScore, double driverIdScore) {
        this.referenceCodeScore = referenceCodeScore;
        this.amountScore = amountScore;
        this.timingScore = timingScore;
        this.driverIdScore = driverIdScore;
        this.weightedTotal = calculateWeightedTotal();
    }

    private double calculateWeightedTotal() {
        return (referenceCodeScore * 0.35) + 
               (amountScore * 0.35) + 
               (timingScore * 0.20) + 
               (driverIdScore * 0.10);
    }

    // Getters and Setters
    public double getReferenceCodeScore() {
        return referenceCodeScore;
    }

    public void setReferenceCodeScore(double referenceCodeScore) {
        this.referenceCodeScore = referenceCodeScore;
        this.weightedTotal = calculateWeightedTotal();
    }

    public double getAmountScore() {
        return amountScore;
    }

    public void setAmountScore(double amountScore) {
        this.amountScore = amountScore;
        this.weightedTotal = calculateWeightedTotal();
    }

    public double getTimingScore() {
        return timingScore;
    }

    public void setTimingScore(double timingScore) {
        this.timingScore = timingScore;
        this.weightedTotal = calculateWeightedTotal();
    }

    public double getDriverIdScore() {
        return driverIdScore;
    }

    public void setDriverIdScore(double driverIdScore) {
        this.driverIdScore = driverIdScore;
        this.weightedTotal = calculateWeightedTotal();
    }

    public double getWeightedTotal() {
        return weightedTotal;
    }

    @Override
    public String toString() {
        return "ScoringBreakdown{" +
                "refScore=" + referenceCodeScore +
                ", amtScore=" + amountScore +
                ", timeScore=" + timingScore +
                ", driverScore=" + driverIdScore +
                ", weighted=" + weightedTotal +
                '}';
    }
}
