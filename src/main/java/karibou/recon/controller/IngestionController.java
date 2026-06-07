package karibou.recon.controller;

import karibou.recon.dto.IngestionResult;
import karibou.recon.model.AgentDeposit;
import karibou.recon.model.DriverCollection;
import karibou.recon.model.Order;
import karibou.recon.service.IngestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for data ingestion endpoints.
 * Handles POST /ingest/orders, /ingest/collections, /ingest/deposits
 */
@RestController
@RequestMapping("/api/v1/reconciliation/ingest")
public class IngestionController {

    @Autowired
    private IngestionService ingestionService;

    /**
     * EP-001: POST /ingest/orders
     * Ingest a batch of orders expecting cash payment.
     */
    @PostMapping("/orders")
    public ResponseEntity<IngestionResult> ingestOrders(@RequestBody Map<String, List<Order>> request) {
        List<Order> orders = request.get("orders");
        
        if (orders == null || orders.isEmpty()) {
            IngestionResult error = new IngestionResult();
            error.addError("No orders provided in request body");
            return ResponseEntity.badRequest().body(error);
        }

        IngestionResult result = ingestionService.ingestOrders(orders);
        
        if (result.getIngestedCount() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * EP-002: POST /ingest/collections
     * Ingest driver collection logs.
     */
    @PostMapping("/collections")
    public ResponseEntity<IngestionResult> ingestCollections(@RequestBody Map<String, List<DriverCollection>> request) {
        List<DriverCollection> collections = request.get("collections");
        
        if (collections == null || collections.isEmpty()) {
            IngestionResult error = new IngestionResult();
            error.addError("No collections provided in request body");
            return ResponseEntity.badRequest().body(error);
        }

        IngestionResult result = ingestionService.ingestCollections(collections);
        
        if (result.getIngestedCount() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * EP-003: POST /ingest/deposits
     * Ingest agent deposit confirmations.
     */
    @PostMapping("/deposits")
    public ResponseEntity<IngestionResult> ingestDeposits(@RequestBody Map<String, List<AgentDeposit>> request) {
        List<AgentDeposit> deposits = request.get("deposits");
        
        if (deposits == null || deposits.isEmpty()) {
            IngestionResult error = new IngestionResult();
            error.addError("No deposits provided in request body");
            return ResponseEntity.badRequest().body(error);
        }

        IngestionResult result = ingestionService.ingestDeposits(deposits);
        
        if (result.getIngestedCount() > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}
