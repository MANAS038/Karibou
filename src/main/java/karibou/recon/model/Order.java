package karibou.recon.model;

import java.time.Instant;

/**
 * Represents a customer order expecting cash payment on delivery.
 */
public class Order {
    private String orderId;
    private String customerId;
    private String customerName;
    private String customerPhone;
    private double expectedAmount;
    private Currency currency;
    private Instant orderCreatedAt;
    private String deliveryAddress;
    private String assignedDriverId;
    private OrderStatus status;
    private String region;

    // Constructors
    public Order() {
    }

    public Order(String orderId, String customerId, double expectedAmount, Currency currency, 
                 Instant orderCreatedAt, OrderStatus status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.expectedAmount = expectedAmount;
        this.currency = currency;
        this.orderCreatedAt = orderCreatedAt;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public double getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(double expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Instant getOrderCreatedAt() {
        return orderCreatedAt;
    }

    public void setOrderCreatedAt(Instant orderCreatedAt) {
        this.orderCreatedAt = orderCreatedAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getAssignedDriverId() {
        return assignedDriverId;
    }

    public void setAssignedDriverId(String assignedDriverId) {
        this.assignedDriverId = assignedDriverId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", expectedAmount=" + expectedAmount +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }
}
