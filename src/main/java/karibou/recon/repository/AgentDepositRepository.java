package karibou.recon.repository;

import karibou.recon.model.AgentDeposit;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory repository for storing and querying AgentDeposit records.
 */
public class AgentDepositRepository {
    private final Map<String, AgentDeposit> deposits = new ConcurrentHashMap<>();

    public void save(AgentDeposit deposit) {
        if (deposit == null || deposit.getDepositId() == null) {
            throw new IllegalArgumentException("Deposit and depositId cannot be null");
        }
        deposits.put(deposit.getDepositId(), deposit);
    }

    public void saveAll(List<AgentDeposit> depositList) {
        if (depositList != null) {
            depositList.forEach(this::save);
        }
    }

    public Optional<AgentDeposit> findById(String depositId) {
        return Optional.ofNullable(deposits.get(depositId));
    }

    public List<AgentDeposit> findAll() {
        return new ArrayList<>(deposits.values());
    }

    public List<AgentDeposit> findByDateRange(Instant start, Instant end) {
        return deposits.values().stream()
                .filter(d -> !d.getDepositTimestamp().isBefore(start) && !d.getDepositTimestamp().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<AgentDeposit> findByAgentId(String agentId) {
        return deposits.values().stream()
                .filter(d -> agentId.equals(d.getAgentId()))
                .collect(Collectors.toList());
    }

    public List<AgentDeposit> findByDriverId(String driverId) {
        return deposits.values().stream()
                .filter(d -> driverId.equals(d.getDriverId()))
                .collect(Collectors.toList());
    }

    public List<AgentDeposit> findByReferenceCode(String referenceCode) {
        return deposits.values().stream()
                .filter(d -> referenceCode != null && referenceCode.equals(d.getReferenceCode()))
                .collect(Collectors.toList());
    }

    public void deleteById(String depositId) {
        deposits.remove(depositId);
    }

    public void clear() {
        deposits.clear();
    }

    public int count() {
        return deposits.size();
    }
}
