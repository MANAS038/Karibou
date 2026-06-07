package karibou.recon.repository;

import karibou.recon.model.EntityType;
import karibou.recon.model.FraudPattern;
import karibou.recon.model.FraudPatternType;
import karibou.recon.model.Severity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying FraudPattern records.
 */
public class FraudPatternRepository {
    private final Map<String, FraudPattern> patterns = new ConcurrentHashMap<>();

    public void save(FraudPattern pattern) {
        if (pattern == null || pattern.getPatternId() == null) {
            throw new IllegalArgumentException("Pattern and patternId cannot be null");
        }
        patterns.put(pattern.getPatternId(), pattern);
    }

    public void saveAll(List<FraudPattern> patternList) {
        if (patternList != null) {
            patternList.forEach(this::save);
        }
    }

    public Optional<FraudPattern> findById(String patternId) {
        return Optional.ofNullable(patterns.get(patternId));
    }

    public List<FraudPattern> findAll() {
        return new ArrayList<>(patterns.values());
    }

    public List<FraudPattern> findByType(FraudPatternType type) {
        return patterns.values().stream()
                .filter(p -> type == p.getPatternType())
                .collect(Collectors.toList());
    }

    public List<FraudPattern> findBySeverity(Severity severity) {
        return patterns.values().stream()
                .filter(p -> severity == p.getSeverity())
                .collect(Collectors.toList());
    }

    public List<FraudPattern> findByEntityType(EntityType entityType) {
        return patterns.values().stream()
                .filter(p -> entityType == p.getEntityType())
                .collect(Collectors.toList());
    }

    public List<FraudPattern> findByEntityId(String entityId) {
        return patterns.values().stream()
                .filter(p -> entityId.equals(p.getEntityId()))
                .collect(Collectors.toList());
    }

    public void deleteById(String patternId) {
        patterns.remove(patternId);
    }

    public void clear() {
        patterns.clear();
    }

    public int count() {
        return patterns.size();
    }
}
