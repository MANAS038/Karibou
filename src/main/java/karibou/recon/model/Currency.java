package karibou.recon.model;

/**
 * Supported currencies for Karibou Express operations across Kenya, Uganda, and Tanzania.
 */
public enum Currency {
    KES("Kenyan Shilling"),
    UGX("Ugandan Shilling"),
    TZS("Tanzanian Shilling");

    private final String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
