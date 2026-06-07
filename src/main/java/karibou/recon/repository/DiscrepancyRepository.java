package karibou.recon.repository;

import karibou.recon.model.Discrepancy;
import karibou.recon.model.DiscrepancyType;
import karibou.recon.model.Severity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying Discrepancy records.
 */
public class DiscrepancyRepository {
    private final Map<String, Discrepancy> discrepancies = new ConcurrentHashMap<>();

    public void save(Discrepancy discrepancy) {
        if (discrepancy == null || discrepancy.getDiscrepancyId() == null) {
            throw new IllegalArgumentException("Discrepancy and discrepancyId cannot be null");
        }
        discrepancies.put(discrepancy.getDiscrepancyId(), discrepancy);
    }

    public void saveAll(List<Discrepancy> discrepancyList) {
        if (discrepancyList != null) {
            discrepancyList.forEach(this::save);
        }
    }

    public Optional<Discrepancy> findById(String discrepancyId) {
        return Optional.ofNullable(discrepancies.get(discrepancyId));
    }

    public List<Discrepancy> findAll() {
        return new ArrayList<>(discrepancies.values());
    }

    public List<Discrepancy> findByType(DiscrepancyType type) {
        return discrepancies.values().stream()
                .filter(d -> type == d.getDiscrepancyType())
                .collect(Collectors.toList());
    }

    public List<Discrepancy> findBySeverity(Severity severity) {
        return discrepancies.values().stream()
                .filter(d -> severity == d.getSeverity())
                .collect(Collectors.toList());
    }

    public List<Discrepancy> findByDriverId(String driverId) {
        return discrepancies.values().stream()
                .filter(d -> driverId.equals(d.getAffectedDriverId()))
                .collect(Collectors.toList());
    }

    public List<Discrepancy> findByAgentId(String agentId) {
        return discrepancies.values().stream()
                .filter(d -> agentId.equals(d.getAffectedAgentId()))
                .collect(Collectors.toList());
    }

    public List<Discrepancy> findUnresolved() {
        return discrepancies.values().stream()
                .filter(d -> d.getResolvedAt() == null)
                .collect(Collectors.toList());
    }

    public void deleteById(String discrepancyId) {
        discrepancies.remove(discrepancyId);
    }

    public void clear() {
        discrepancies.clear();
    }

    public int count() {
        return discrepancies.size();
    }
}
