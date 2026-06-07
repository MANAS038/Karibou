package karibou.recon.model;

/**
 * Type of match between order, collection, and deposit.
 */
public enum MatchType {
    /**
     * Single order-collection-deposit match
     */
    ONE_TO_ONE,
    
    /**
     * Multiple orders/collections batched into one deposit
     */
    MANY_TO_ONE
}
