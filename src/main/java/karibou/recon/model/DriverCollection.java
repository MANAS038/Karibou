package karibou.recon.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Log entry from driver mobile app recording cash collected from customer(s).
 * Supports batching - one collection can reference multiple order IDs.
 */
public class DriverCollection {
    private String collectionId;
    private String driverId;
    private String driverName;
    private List<String> orderIds;
    private double collectedAmount;
    private Currency currency;
    private Instant collectionTimestamp;
    private String referenceCode;
    private String gpsLocation;
    private String notes;

    // Constructors
    public DriverCollection() {
        this.orderIds = new ArrayList<>();
    }

    public DriverCollection(String collectionId, String driverId, List<String> orderIds,
                            double collectedAmount, Currency currency, Instant collectionTimestamp) {
        this.collectionId = collectionId;
        this.driverId = driverId;
        this.orderIds = orderIds != null ? new ArrayList<>(orderIds) : new ArrayList<>();
        this.collectedAmount = collectedAmount;
        this.currency = currency;
        this.collectionTimestamp = collectionTimestamp;
    }

    // Getters and Setters
    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public List<String> getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(List<String> orderIds) {
        this.orderIds = orderIds != null ? new ArrayList<>(orderIds) : new ArrayList<>();
    }

    public double getCollectedAmount() {
        return collectedAmount;
    }

    public void setCollectedAmount(double collectedAmount) {
        this.collectedAmount = collectedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Instant getCollectionTimestamp() {
        return collectionTimestamp;
    }

    public void setCollectionTimestamp(Instant collectionTimestamp) {
        this.collectionTimestamp = collectionTimestamp;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getGpsLocation() {
        return gpsLocation;
    }

    public void setGpsLocation(String gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "DriverCollection{" +
                "collectionId='" + collectionId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", orderIds=" + orderIds +
                ", collectedAmount=" + collectedAmount +
                ", currency=" + currency +
                ", collectionTimestamp=" + collectionTimestamp +
                '}';
    }
}
