package karibou.recon.controller;

import karibou.recon.model.DiscrepancyType;
import karibou.recon.model.Severity;
import karibou.recon.service.DiscrepancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for discrepancy management.
 * Handles GET /discrepancies and PATCH /discrepancies/{id}/resolve
 */
@RestController
@RequestMapping("/api/v1/reconciliation/discrepancies")
public class DiscrepancyController {

    @Autowired
    private DiscrepancyService discrepancyService;

    /**
     * EP-008: GET /discrepancies
     * Query all detected discrepancies.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> queryDiscrepancies(
            @RequestParam(required = false) String discrepancyType,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) Boolean resolved,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int pageSize) {

        // Basic validation
        if (page < 1 || pageSize < 1 || pageSize > 100) {
            return ResponseEntity.badRequest().build();
        }

        try {
            DiscrepancyType typeFilter = discrepancyType != null ? DiscrepancyType.valueOf(discrepancyType) : null;
            Severity severityFilter = severity != null ? Severity.valueOf(severity) : null;

            Map<String, Object> response = discrepancyService.queryDiscrepancies(
                    typeFilter, severityFilter, resolved, page, pageSize);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * EP-009: PATCH /discrepancies/{discrepancy_id}/resolve
     * Resolve a discrepancy.
     */
    @PatchMapping("/{discrepancyId}/resolve")
    public ResponseEntity<String> resolveDiscrepancy(
            @PathVariable String discrepancyId,
            @RequestBody Map<String, String> request) {

        // Basic validation
        if (discrepancyId == null || discrepancyId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid discrepancy ID");
        }

        String resolvedBy = request.get("resolved_by");
        String resolutionNotes = request.get("resolution_notes");

        String result = discrepancyService.resolveDiscrepancy(discrepancyId, resolvedBy, resolutionNotes);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else if ("already_resolved".equals(result)) {
            return ResponseEntity.status(409).body("Discrepancy already resolved");
        } else {
            return ResponseEntity.ok("Discrepancy resolved successfully");
        }
    }
}
