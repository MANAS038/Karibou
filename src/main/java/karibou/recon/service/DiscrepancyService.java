package karibou.recon.service;

import karibou.recon.model.Discrepancy;
import karibou.recon.model.DiscrepancyType;
import karibou.recon.model.Severity;
import karibou.recon.repository.DiscrepancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing discrepancies.
 */
@Service
public class DiscrepancyService {

    @Autowired
    private DiscrepancyRepository discrepancyRepository;

    /**
     * Query discrepancies with optional filtering and pagination.
     */
    public Map<String, Object> queryDiscrepancies(
            DiscrepancyType discrepancyType,
            Severity severity,
            Boolean resolved,
            int page,
            int pageSize) {

        List<Discrepancy> allDiscrepancies = discrepancyRepository.findAll();

        // Apply filters
        if (discrepancyType != null) {
            allDiscrepancies = allDiscrepancies.stream()
                    .filter(d -> d.getDiscrepancyType() == discrepancyType)
                    .collect(Collectors.toList());
        }

        if (severity != null) {
            allDiscrepancies = allDiscrepancies.stream()
                    .filter(d -> d.getSeverity() == severity)
                    .collect(Collectors.toList());
        }

        if (resolved != null) {
            allDiscrepancies = allDiscrepancies.stream()
                    .filter(d -> resolved.equals(d.getResolvedAt() != null))
                    .collect(Collectors.toList());
        }

        // Pagination
        int totalCount = allDiscrepancies.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);
        List<Discrepancy> paginatedResults = allDiscrepancies.subList(
                Math.max(0, startIndex),
                Math.max(0, endIndex)
        );

        Map<String, Object> response = new HashMap<>();
        response.put("total_count", totalCount);
        response.put("page", page);
        response.put("page_size", pageSize);
        response.put("discrepancies", paginatedResults);

        return response;
    }

    /**
     * Resolve a discrepancy.
     * Returns true if resolved successfully, false if already resolved, null if not found.
     */
    public String resolveDiscrepancy(String discrepancyId, String resolvedBy, String resolutionNotes) {
        Optional<Discrepancy> optDiscrepancy = discrepancyRepository.findById(discrepancyId);

        if (!optDiscrepancy.isPresent()) {
            return null; // Not found
        }

        Discrepancy discrepancy = optDiscrepancy.get();

        if (discrepancy.getResolvedAt() != null) {
            return "already_resolved"; // Already resolved
        }

        discrepancy.setResolvedAt(Instant.now());
        discrepancy.setResolvedBy(resolvedBy);
        discrepancy.setResolutionNotes(resolutionNotes);

        discrepancyRepository.save(discrepancy);

        return "success";
    }
}
