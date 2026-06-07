package karibou.recon.model;

/**
 * Lifecycle status of an order.
 */
public enum OrderStatus {
    PENDING_DELIVERY,
    DELIVERED_CASH_PENDING,
    DELIVERED_CASH_COLLECTED,
    RECONCILED,
    DISPUTED
}
