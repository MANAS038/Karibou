package karibou.recon.controller;

import karibou.recon.model.MatchQuality;
import karibou.recon.model.ReconciliationResult;
import karibou.recon.model.ReconciliationStatus;
import karibou.recon.service.ResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST controller for querying reconciliation results.
 * Handles GET /results and GET /results/{id}
 */
@RestController
@RequestMapping("/api/v1/reconciliation/results")
public class ResultsController {

    @Autowired
    private ResultsService resultsService;

    /**
     * EP-006: GET /results
     * Query reconciliation results with filtering.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> queryResults(
            @RequestParam(required = false) String matchQuality,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        // Basic validation
        if (page < 1 || pageSize < 1 || pageSize > 100) {
            return ResponseEntity.badRequest().build();
        }

        try {
            MatchQuality qualityFilter = matchQuality != null ? MatchQuality.valueOf(matchQuality) : null;
            ReconciliationStatus statusFilter = status != null ? ReconciliationStatus.valueOf(status) : null;

            Map<String, Object> response = resultsService.queryResults(qualityFilter, statusFilter, page, pageSize);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * EP-007: GET /results/{reconciliation_id}
     * Get a single reconciliation result by ID.
     */
    @GetMapping("/{reconciliationId}")
    public ResponseEntity<ReconciliationResult> getResult(@PathVariable String reconciliationId) {
        // Basic validation
        if (reconciliationId == null || reconciliationId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ReconciliationResult> result = resultsService.getResultById(reconciliationId);
        return result.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
