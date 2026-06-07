package karibou.recon.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Result of an ingestion operation.
 */
public class IngestionResult {
    private int ingestedCount;
    private int skippedCount;
    private List<String> errors;

    public IngestionResult() {
        this.errors = new ArrayList<>();
    }

    public IngestionResult(int ingestedCount, int skippedCount) {
        this();
        this.ingestedCount = ingestedCount;
        this.skippedCount = skippedCount;
    }

    public int getIngestedCount() {
        return ingestedCount;
    }

    public void setIngestedCount(int ingestedCount) {
        this.ingestedCount = ingestedCount;
    }

    public int getSkippedCount() {
        return skippedCount;
    }

    public void setSkippedCount(int skippedCount) {
        this.skippedCount = skippedCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }
}
