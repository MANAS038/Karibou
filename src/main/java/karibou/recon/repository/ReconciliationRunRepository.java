package karibou.recon.repository;

import karibou.recon.model.ReconciliationRun;
import karibou.recon.model.RunStatus;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying ReconciliationRun records.
 */
public class ReconciliationRunRepository {
    private final Map<String, ReconciliationRun> runs = new ConcurrentHashMap<>();

    public void save(ReconciliationRun run) {
        if (run == null || run.getRunId() == null) {
            throw new IllegalArgumentException("Run and runId cannot be null");
        }
        runs.put(run.getRunId(), run);
    }

    public Optional<ReconciliationRun> findById(String runId) {
        return Optional.ofNullable(runs.get(runId));
    }

    public List<ReconciliationRun> findAll() {
        return new ArrayList<>(runs.values());
    }

    public List<ReconciliationRun> findByStatus(RunStatus status) {
        return runs.values().stream()
                .filter(r -> status == r.getStatus())
                .collect(Collectors.toList());
    }

    public void deleteById(String runId) {
        runs.remove(runId);
    }

    public void clear() {
        runs.clear();
    }

    public int count() {
        return runs.size();
    }
}
