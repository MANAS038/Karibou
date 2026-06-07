package karibou.recon.service;

import karibou.recon.dto.IngestionResult;
import karibou.recon.model.AgentDeposit;
import karibou.recon.model.DriverCollection;
import karibou.recon.model.Order;
import karibou.recon.repository.AgentDepositRepository;
import karibou.recon.repository.DriverCollectionRepository;
import karibou.recon.repository.OrderRepository;

import java.util.List;

/**
 * Service for ingesting orders, driver collections, and agent deposits.
 * Handles validation and normalization before storage.
 */
public class IngestionService {
    private final OrderRepository orderRepository;
    private final DriverCollectionRepository collectionRepository;
    private final AgentDepositRepository depositRepository;

    public IngestionService(OrderRepository orderRepository,
                           DriverCollectionRepository collectionRepository,
                           AgentDepositRepository depositRepository) {
        this.orderRepository = orderRepository;
        this.collectionRepository = collectionRepository;
        this.depositRepository = depositRepository;
    }

    /**
     * Ingest a batch of orders. Idempotent - re-ingesting the same order_id updates the record.
     */
    public IngestionResult ingestOrders(List<Order> orders) {
        IngestionResult result = new IngestionResult();
        
        if (orders == null || orders.isEmpty()) {
            result.addError("No orders provided");
            return result;
        }

        int ingested = 0;
        int skipped = 0;

        for (Order order : orders) {
            try {
                validateOrder(order);
                normalizeOrder(order);
                orderRepository.save(order);
                ingested++;
            } catch (IllegalArgumentException e) {
                result.addError("Order " + order.getOrderId() + ": " + e.getMessage());
                skipped++;
            }
        }

        result.setIngestedCount(ingested);
        result.setSkippedCount(skipped);
        return result;
    }

    /**
     * Ingest a batch of driver collections.
     */
    public IngestionResult ingestCollections(List<DriverCollection> collections) {
        IngestionResult result = new IngestionResult();
        
        if (collections == null || collections.isEmpty()) {
            result.addError("No collections provided");
            return result;
        }

        int ingested = 0;
        int skipped = 0;

        for (DriverCollection collection : collections) {
            try {
                validateCollection(collection);
                normalizeCollection(collection);
                collectionRepository.save(collection);
                ingested++;
            } catch (IllegalArgumentException e) {
                result.addError("Collection " + collection.getCollectionId() + ": " + e.getMessage());
                skipped++;
            }
        }

        result.setIngestedCount(ingested);
        result.setSkippedCount(skipped);
        return result;
    }

    /**
     * Ingest a batch of agent deposits.
     */
    public IngestionResult ingestDeposits(List<AgentDeposit> deposits) {
        IngestionResult result = new IngestionResult();
        
        if (deposits == null || deposits.isEmpty()) {
            result.addError("No deposits provided");
            return result;
        }

        int ingested = 0;
        int skipped = 0;

        for (AgentDeposit deposit : deposits) {
            try {
                validateDeposit(deposit);
                normalizeDeposit(deposit);
                depositRepository.save(deposit);
                ingested++;
            } catch (IllegalArgumentException e) {
                result.addError("Deposit " + deposit.getDepositId() + ": " + e.getMessage());
                skipped++;
            }
        }

        result.setIngestedCount(ingested);
        result.setSkippedCount(skipped);
        return result;
    }

    // Validation methods
    private void validateOrder(Order order) {
        if (order.getOrderId() == null || order.getOrderId().trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (order.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (order.getExpectedAmount() <= 0) {
            throw new IllegalArgumentException("Expected amount must be positive");
        }
        if (order.getCurrency() == null) {
            throw new IllegalArgumentException("Currency is required");
        }
        if (order.getOrderCreatedAt() == null) {
            throw new IllegalArgumentException("Order created timestamp is required");
        }
        if (order.getStatus() == null) {
            throw new IllegalArgumentException("Order status is required");
        }
    }

    private void validateCollection(DriverCollection collection) {
        if (collection.getCollectionId() == null || collection.getCollectionId().trim().isEmpty()) {
            throw new IllegalArgumentException("Collection ID is required");
        }
        if (collection.getDriverId() == null) {
            throw new IllegalArgumentException("Driver ID is required");
        }
        if (collection.getOrderIds() == null || collection.getOrderIds().isEmpty()) {
            throw new IllegalArgumentException("At least one order ID is required");
        }
        if (collection.getCollectedAmount() <= 0) {
            throw new IllegalArgumentException("Collected amount must be positive");
        }
    }

    private void validateDeposit(AgentDeposit deposit) {
        if (deposit.getDepositId() == null || deposit.getDepositId().trim().isEmpty()) {
            throw new IllegalArgumentException("Deposit ID is required");
        }
        if (deposit.getAgentId() == null) {
            throw new IllegalArgumentException("Agent ID is required");
        }
        if (deposit.getDepositedAmount() <= 0) {
            throw new IllegalArgumentException("Deposited amount must be positive");
        }
        if (deposit.getCurrency() == null) {
            throw new IllegalArgumentException("Currency is required");
        }
        if (deposit.getDepositTimestamp() == null) {
            throw new IllegalArgumentException("Deposit timestamp is required");
        }
    }

    // Normalization methods
    private void normalizeOrder(Order order) {
        // Trim whitespace from strings
        if (order.getOrderId() != null) {
            order.setOrderId(order.getOrderId().trim());
        }
        if (order.getAssignedDriverId() != null) {
            order.setAssignedDriverId(order.getAssignedDriverId().trim());
        }
    }

    private void normalizeCollection(DriverCollection collection) {
        // Normalize reference code - strip whitespace, uppercase
        if (collection.getReferenceCode() != null) {
            collection.setReferenceCode(collection.getReferenceCode().trim().toUpperCase());
        }
        if (collection.getCollectionId() != null) {
            collection.setCollectionId(collection.getCollectionId().trim());
        }
    }

    private void normalizeDeposit(AgentDeposit deposit) {
        // Normalize reference code - strip whitespace, uppercase
        if (deposit.getReferenceCode() != null) {
            deposit.setReferenceCode(deposit.getReferenceCode().trim().toUpperCase());
        }
        if (deposit.getDepositId() != null) {
            deposit.setDepositId(deposit.getDepositId().trim());
        }
    }
}
