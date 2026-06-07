package karibou.recon.repository;

import karibou.recon.model.Order;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying Order records.
 * Thread-safe using ConcurrentHashMap.
 */
public class OrderRepository {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    public void save(Order order) {
        if (order == null || order.getOrderId() == null) {
            throw new IllegalArgumentException("Order and orderId cannot be null");
        }
        orders.put(order.getOrderId(), order);
    }

    public void saveAll(List<Order> orderList) {
        if (orderList != null) {
            orderList.forEach(this::save);
        }
    }

    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> findByDateRange(Instant start, Instant end) {
        return orders.values().stream()
                .filter(o -> !o.getOrderCreatedAt().isBefore(start) && !o.getOrderCreatedAt().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Order> findByDriverId(String driverId) {
        return orders.values().stream()
                .filter(o -> driverId.equals(o.getAssignedDriverId()))
                .collect(Collectors.toList());
    }

    public List<Order> findByRegion(String region) {
        return orders.values().stream()
                .filter(o -> region.equals(o.getRegion()))
                .collect(Collectors.toList());
    }

    public void deleteById(String orderId) {
        orders.remove(orderId);
    }

    public void clear() {
        orders.clear();
    }

    public int count() {
        return orders.size();
    }

    public boolean exists(String orderId) {
        return orders.containsKey(orderId);
    }
}
