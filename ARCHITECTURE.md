# Karibou Express Reconciliation Engine - Architecture Documentation

## Project Overview

This is a **Spring Boot REST API** implementing the Karibou Express Cash Reconciliation Engine v2.0, designed to automatically match 18,000+ monthly cash-on-delivery (COD) transactions across three data streams:
1. **Orders** (e-commerce platform)
2. **Driver Collections** (mobile app logs)
3. **Agent Deposits** (payment agent confirmations)

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** for build management
- **In-memory storage** (ConcurrentHashMap-based repositories)

## Package Structure

```
src/karibou/recon/
├── model/                  # Domain models (POJOs)
│   ├── Order.java
│   ├── DriverCollection.java
│   ├── AgentDeposit.java
│   ├── ReconciliationResult.java
│   ├── Discrepancy.java
│   ├── FraudPattern.java
│   ├── ReconciliationRun.java
│   ├── ScoringBreakdown.java
│   ├── ReconciliationRunSummary.java
│   └── [11 enum classes]   # Currency, OrderStatus, MatchType, MatchQuality, etc.
│
├── repository/             # Data access layer (in-memory)
│   ├── OrderRepository.java
│   ├── DriverCollectionRepository.java
│   ├── AgentDepositRepository.java
│   ├── ReconciliationResultRepository.java
│   ├── DiscrepancyRepository.java
│   ├── FraudPatternRepository.java
│   └── ReconciliationRunRepository.java
│
├── service/                # Business logic
│   ├── IngestionService.java         # Data ingestion + validation
│   ├── MatchingEngine.java           # Core 10-step matching pipeline
│   └── RunService.java                # Orchestrates reconciliation runs
│
├── controller/             # REST API endpoints
│   ├── IngestionController.java      # POST /ingest/{orders|collections|deposits}
│   ├── RunController.java            # POST /run, GET /run/{id}
│   ├── ResultsController.java        # GET /results
│   └── DiscrepancyController.java    # GET /discrepancies, PATCH resolve
│
├── config/                 # Configuration
│   └── EngineConfig.java             # Tunable thresholds and weights
│
├── dto/                    # Data Transfer Objects
│   ├── IngestionResult.java
│   └── RunRequest.java
│
└── KaribouReconciliationApplication.java   # Spring Boot main class
```

## Core Algorithms

### Matching Engine (10-Step Pipeline)

Based on `idea.json` specification:

1. **Ingest & Normalize**: Timestamps → UTC, trim/uppercase reference codes
2. **Exact Reference Match**: Direct string equality + driver ID match
3. **Fuzzy Reference Match**: Levenshtein distance ≤ 2, prefix matching
4. **Amount Matching**: % variance + absolute tolerance per currency (KES: 50, UGX: 2000, TZS: 1500)
5. **Timing Validation**: order_created < collection < deposit, with scoring
6. **Driver ID Correlation**: Match across order/collection/deposit
7. **Composite Scoring**: Weighted formula = ref\*0.35 + amt\*0.35 + time\*0.20 + driver\*0.10
8. **Batch Detection**: Subset-sum for multi-order deposits (TODO: not yet implemented)
9. **Discrepancy Detection**: 6 rule types (DR-001 to DR-006)
10. **Fraud Pattern Analysis**: 30-day rolling window (TODO: not yet implemented)

### Match Quality Tiers

| Tier | Confidence Score | Behavior |
|------|------------------|----------|
| EXACT_MATCH | ≥ 0.95 | Auto-reconcile |
| PROBABLE_MATCH | 0.75 - 0.94 | Auto-reconcile with audit trail |
| POSSIBLE_MATCH | 0.50 - 0.74 | Queue for manual review |
| UNMATCHED | < 0.50 | Finance team investigation |

## API Endpoints (Implemented)

| Method | Endpoint | Controller | Status |
|--------|----------|------------|--------|
| POST | `/api/v1/reconciliation/ingest/orders` | IngestionController | ✅ |
| POST | `/api/v1/reconciliation/ingest/collections` | IngestionController | ✅ |
| POST | `/api/v1/reconciliation/ingest/deposits` | IngestionController | ✅ |
| POST | `/api/v1/reconciliation/run` | RunController | ✅ |
| GET | `/api/v1/reconciliation/run/{runId}` | RunController | ✅ |
| GET | `/api/v1/reconciliation/results` | ResultsController | ✅ |
| GET | `/api/v1/reconciliation/results/{id}` | ResultsController | ✅ |
| GET | `/api/v1/reconciliation/discrepancies` | DiscrepancyController | ✅ |
| PATCH | `/api/v1/reconciliation/discrepancies/{id}/resolve` | DiscrepancyController | ✅ |

## Data Flow

```
[Data Ingestion]
    ↓
Orders/Collections/Deposits → IngestionService
    ↓ (validation + normalization)
Repositories (in-memory)
    ↓
[Trigger Run] → RunService → MatchingEngine
    ↓
Scoring Algorithm
    ↓
ReconciliationResults + Discrepancies
    ↓
[Query Results] via REST API
```

## Configuration (EngineConfig)

All thresholds are tunable:

- **Amount tolerance**: 2% or currency-specific absolutes
- **Fuzzy matching**: Max edit distance = 2
- **Timing windows**: Collection→Deposit ≤ 72h, Order→Collection ≤ 24h
- **Auto-reconcile threshold**: Confidence ≥ 0.95
- **Fraud detection**: Driver shortfall rate > 30%, Agent orphan rate > 20%

## Next Steps / TODOs

1. **Batch Deposit Detection (Step 8)**: Implement subset-sum algorithm for many-to-one matching
2. **Fraud Pattern Analysis (Step 10)**: Implement 6 fraud pattern rules (FP-001 to FP-006)
3. **Database Integration**: Replace in-memory repositories with JPA/Hibernate + PostgreSQL
4. **Manual Review Queue (SG-2)**: Add endpoints for analyst claim/approve/reject workflows
5. **Multi-Currency Normalization (SG-3)**: Add exchange rate service + base currency conversion
6. **Additional Endpoints**: Implement EP-010 to EP-013 from idea.json
7. **Testing**: Add unit tests for MatchingEngine scoring algorithms
8. **CI/CD**: Add GitHub Actions workflow for build + test + deploy

## Running the Application

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Test
curl http://localhost:8080/api/v1/reconciliation/ingest/orders -X POST \
  -H "Content-Type: application/json" \
  -d '{"orders": [...]}'
```
