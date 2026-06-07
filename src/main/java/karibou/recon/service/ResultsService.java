package karibou.recon.service;

import karibou.recon.model.MatchQuality;
import karibou.recon.model.ReconciliationResult;
import karibou.recon.model.ReconciliationStatus;
import karibou.recon.repository.ReconciliationResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for querying and managing reconciliation results.
 */
@Service
public class ResultsService {

    @Autowired
    private ReconciliationResultRepository resultRepository;

    /**
     * Query results with optional filtering and pagination.
     */
    public Map<String, Object> queryResults(
            MatchQuality matchQuality,
            ReconciliationStatus status,
            int page,
            int pageSize) {

        List<ReconciliationResult> allResults = resultRepository.findAll();

        // Apply filters
        if (matchQuality != null) {
            allResults = allResults.stream()
                    .filter(r -> r.getMatchQuality() == matchQuality)
                    .collect(Collectors.toList());
        }

        if (status != null) {
            allResults = allResults.stream()
                    .filter(r -> r.getStatus() == status)
                    .collect(Collectors.toList());
        }

        // Pagination
        int totalCount = allResults.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalCount);
        List<ReconciliationResult> paginatedResults = allResults.subList(
                Math.max(0, startIndex),
                Math.max(0, endIndex)
        );

        Map<String, Object> response = new HashMap<>();
        response.put("total_count", totalCount);
        response.put("page", page);
        response.put("page_size", pageSize);
        response.put("results", paginatedResults);

        return response;
    }

    /**
     * Get a single reconciliation result by ID.
     */
    public Optional<ReconciliationResult> getResultById(String reconciliationId) {
        return resultRepository.findById(reconciliationId);
    }
}
