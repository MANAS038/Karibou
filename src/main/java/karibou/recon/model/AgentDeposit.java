package karibou.recon.model;

import java.time.Instant;

/**
 * Deposit confirmation from a payment agent (mobile money kiosk, bank branch, retail shop).
 * Reference codes are frequently misspelled, partial, or missing.
 */
public class AgentDeposit {
    private String depositId;
    private String agentId;
    private String agentName;
    private String agentLocation;
    private AgentType agentType;
    private String driverId;
    private String referenceCode;
    private double depositedAmount;
    private Currency currency;
    private Instant depositTimestamp;
    private String receiptNumber;
    private String notes;

    // Constructors
    public AgentDeposit() {
    }

    public AgentDeposit(String depositId, String agentId, double depositedAmount, 
                        Currency currency, Instant depositTimestamp) {
        this.depositId = depositId;
        this.agentId = agentId;
        this.depositedAmount = depositedAmount;
        this.currency = currency;
        this.depositTimestamp = depositTimestamp;
    }

    // Getters and Setters
    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentLocation() {
        return agentLocation;
    }

    public void setAgentLocation(String agentLocation) {
        this.agentLocation = agentLocation;
    }

    public AgentType getAgentType() {
        return agentType;
    }

    public void setAgentType(AgentType agentType) {
        this.agentType = agentType;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public double getDepositedAmount() {
        return depositedAmount;
    }

    public void setDepositedAmount(double depositedAmount) {
        this.depositedAmount = depositedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Instant getDepositTimestamp() {
        return depositTimestamp;
    }

    public void setDepositTimestamp(Instant depositTimestamp) {
        this.depositTimestamp = depositTimestamp;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AgentDeposit{" +
                "depositId='" + depositId + '\'' +
                ", agentId='" + agentId + '\'' +
                ", depositedAmount=" + depositedAmount +
                ", currency=" + currency +
                ", depositTimestamp=" + depositTimestamp +
                '}';
    }
}
