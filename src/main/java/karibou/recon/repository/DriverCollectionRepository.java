package karibou.recon.repository;

import karibou.recon.model.DriverCollection;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying DriverCollection records.
 */
public class DriverCollectionRepository {
    private final Map<String, DriverCollection> collections = new ConcurrentHashMap<>();

    public void save(DriverCollection collection) {
        if (collection == null || collection.getCollectionId() == null) {
            throw new IllegalArgumentException("Collection and collectionId cannot be null");
        }
        collections.put(collection.getCollectionId(), collection);
    }

    public void saveAll(List<DriverCollection> collectionList) {
        if (collectionList != null) {
            collectionList.forEach(this::save);
        }
    }

    public Optional<DriverCollection> findById(String collectionId) {
        return Optional.ofNullable(collections.get(collectionId));
    }

    public List<DriverCollection> findAll() {
        return new ArrayList<>(collections.values());
    }

    public List<DriverCollection> findByDateRange(Instant start, Instant end) {
        return collections.values().stream()
                .filter(c -> !c.getCollectionTimestamp().isBefore(start) && !c.getCollectionTimestamp().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<DriverCollection> findByDriverId(String driverId) {
        return collections.values().stream()
                .filter(c -> driverId.equals(c.getDriverId()))
                .collect(Collectors.toList());
    }

    public List<DriverCollection> findByOrderId(String orderId) {
        return collections.values().stream()
                .filter(c -> c.getOrderIds().contains(orderId))
                .collect(Collectors.toList());
    }

    public List<DriverCollection> findByReferenceCode(String referenceCode) {
        return collections.values().stream()
                .filter(c -> referenceCode.equals(c.getReferenceCode()))
                .collect(Collectors.toList());
    }

    public void deleteById(String collectionId) {
        collections.remove(collectionId);
    }

    public void clear() {
        collections.clear();
    }

    public int count() {
        return collections.size();
    }
}
