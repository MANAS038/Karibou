package karibou.recon.repository;

import karibou.recon.model.MatchQuality;
import karibou.recon.model.ReconciliationResult;
import karibou.recon.model.ReconciliationStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying ReconciliationResult records.
 */
public class ReconciliationResultRepository {
    private final Map<String, ReconciliationResult> results = new ConcurrentHashMap<>();

    public void save(ReconciliationResult result) {
        if (result == null || result.getReconciliationId() == null) {
            throw new IllegalArgumentException("Result and reconciliationId cannot be null");
        }
        results.put(result.getReconciliationId(), result);
    }

    public void saveAll(List<ReconciliationResult> resultList) {
        if (resultList != null) {
            resultList.forEach(this::save);
        }
    }

    public Optional<ReconciliationResult> findById(String reconciliationId) {
        return Optional.ofNullable(results.get(reconciliationId));
    }

    public List<ReconciliationResult> findAll() {
        return new ArrayList<>(results.values());
    }

    public List<ReconciliationResult> findByMatchQuality(MatchQuality matchQuality) {
        return results.values().stream()
                .filter(r -> matchQuality == r.getMatchQuality())
                .collect(Collectors.toList());
    }

    public List<ReconciliationResult> findByStatus(ReconciliationStatus status) {
        return results.values().stream()
                .filter(r -> status == r.getStatus())
                .collect(Collectors.toList());
    }

    public List<ReconciliationResult> findByDepositId(String depositId) {
        return results.values().stream()
                .filter(r -> depositId.equals(r.getDepositId()))
                .collect(Collectors.toList());
    }

    public void deleteById(String reconciliationId) {
        results.remove(reconciliationId);
    }

    public void clear() {
        results.clear();
    }

    public int count() {
        return results.size();
    }
}
