package karibou.recon.model;

/**
 * Quality tier of a reconciliation match.
 */
public enum MatchQuality {
    /**
     * All IDs/refs/amounts align perfectly. Auto-reconcile.
     */
    EXACT_MATCH,
    
    /**
     * Strong signals with minor fuzzy matches. Auto-reconcile with audit trail.
     */
    PROBABLE_MATCH,
    
    /**
     * Partial signals. Queue for human review.
     */
    POSSIBLE_MATCH,
    
    /**
     * No viable match found. Finance team must investigate.
     */
    UNMATCHED
}
