package karibou.recon.controller;

import karibou.recon.dto.RunRequest;
import karibou.recon.model.ReconciliationRun;
import karibou.recon.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller for reconciliation run management.
 * Handles POST /run and GET /run/{run_id}
 */
@RestController
@RequestMapping("/api/v1/reconciliation")
public class RunController {

    @Autowired
    private RunService runService;

    /**
     * EP-004: POST /run
     * Trigger a reconciliation run.
     */
    @PostMapping("/run")
    public ResponseEntity<ReconciliationRun> startRun(@RequestBody RunRequest request) {
        if (request.getDateRangeStart() == null || request.getDateRangeEnd() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReconciliationRun run = runService.startRun(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(run);
    }

    /**
     * EP-005: GET /run/{run_id}
     * Get reconciliation run status and summary.
     */
    @GetMapping("/run/{runId}")
    public ResponseEntity<ReconciliationRun> getRun(@PathVariable String runId) {
        Optional<ReconciliationRun> run = runService.getRun(runId);
        
        if (run.isPresent()) {
            return ResponseEntity.ok(run.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
