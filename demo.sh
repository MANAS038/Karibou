#!/bin/bash

# Karibou Express Cash Reconciliation Engine - Demo Script
# This script demonstrates all reconciliation scenarios

set -e

BASE_URL="http://localhost:8095/api/v1/reconciliation"
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Karibou Express Reconciliation Demo${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if server is running
echo -e "${YELLOW}Checking if server is running on port 8095...${NC}"
if ! curl -s http://localhost:8095 > /dev/null; then
    echo -e "${RED}Error: Server is not running. Please start it with: ./gradlew bootRun${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Server is running${NC}\n"

# Scenario 1: Exact Match
echo -e "${BLUE}=== Scenario 1: Exact Match ===${NC}"
echo "Loading order..."
curl -s -X POST "$BASE_URL/ingest/orders" \
  -H "Content-Type: application/json" \
  -d '{"orders":[{"order_id":"ORD-2024-001","customer_id":"CUST-56789","expected_amount":1500.0,"currency":"KES","order_created_at":"2024-07-15T08:00:00Z","assigned_driver_id":"DRV-047","status":"DELIVERED_CASH_PENDING","region":"Kenya"}]}'

echo "Loading collection..."
curl -s -X POST "$BASE_URL/ingest/collections" \
  -H "Content-Type: application/json" \
  -d '{"collections":[{"collection_id":"COL-2024-001","driver_id":"DRV-047","order_ids":["ORD-2024-001"],"collected_amount":1500.0,"currency":"KES","collection_timestamp":"2024-07-15T14:00:00Z","reference_code":"KBX-9001"}]}'

echo "Loading deposit..."
curl -s -X POST "$BASE_URL/ingest/deposits" \
  -H "Content-Type: application/json" \
  -d '{"deposits":[{"deposit_id":"DEP-2024-001","agent_id":"AGT-239","driver_id":"DRV-047","reference_code":"KBX-9001","deposited_amount":1500.0,"currency":"KES","deposit_timestamp":"2024-07-15T17:00:00Z"}]}'

echo -e "${GREEN}✓ Scenario 1 data loaded${NC}\n"

# Scenario 2: Fuzzy Match
echo -e "${BLUE}=== Scenario 2: Fuzzy Match (typo + amount variance) ===${NC}"
curl -s -X POST "$BASE_URL/ingest/orders" \
  -H "Content-Type: application/json" \
  -d '{"orders":[{"order_id":"ORD-2024-002","customer_id":"CUST-56790","expected_amount":1500.0,"currency":"KES","order_created_at":"2024-07-15T09:00:00Z","assigned_driver_id":"DRV-047","status":"DELIVERED_CASH_PENDING","region":"Kenya"}]}'

curl -s -X POST "$BASE_URL/ingest/collections" \
  -H "Content-Type: application/json" \
  -d '{"collections":[{"collection_id":"COL-2024-002","driver_id":"DRV-047","order_ids":["ORD-2024-002"],"collected_amount":1500.0,"currency":"KES","collection_timestamp":"2024-07-15T14:30:00Z","reference_code":"KBX-9002"}]}'

curl -s -X POST "$BASE_URL/ingest/deposits" \
  -H "Content-Type: application/json" \
  -d '{"deposits":[{"deposit_id":"DEP-2024-002","agent_id":"AGT-239","driver_id":"DRV-047","reference_code":"KBX-9020","deposited_amount":1450.0,"currency":"KES","deposit_timestamp":"2024-07-15T17:30:00Z"}]}'

echo -e "${GREEN}✓ Scenario 2 data loaded${NC}\n"

# Scenario 3: Batched Deposit
echo -e "${BLUE}=== Scenario 3: Batched Deposit (3 orders → 1 deposit) ===${NC}"
curl -s -X POST "$BASE_URL/ingest/orders" \
  -H "Content-Type: application/json" \
  -d '{"orders":[{"order_id":"ORD-2024-010","customer_id":"CUST-10001","expected_amount":800.0,"currency":"KES","order_created_at":"2024-07-15T10:00:00Z","assigned_driver_id":"DRV-112","status":"DELIVERED_CASH_PENDING","region":"Kenya"},{"order_id":"ORD-2024-011","customer_id":"CUST-10002","expected_amount":1200.0,"currency":"KES","order_created_at":"2024-07-15T10:30:00Z","assigned_driver_id":"DRV-112","status":"DELIVERED_CASH_PENDING","region":"Kenya"},{"order_id":"ORD-2024-012","customer_id":"CUST-10003","expected_amount":650.0,"currency":"KES","order_created_at":"2024-07-15T11:00:00Z","assigned_driver_id":"DRV-112","status":"DELIVERED_CASH_PENDING","region":"Kenya"}]}'

curl -s -X POST "$BASE_URL/ingest/collections" \
  -H "Content-Type: application/json" \
  -d '{"collections":[{"collection_id":"COL-2024-010","driver_id":"DRV-112","order_ids":["ORD-2024-010","ORD-2024-011","ORD-2024-012"],"collected_amount":2650.0,"currency":"KES","collection_timestamp":"2024-07-15T15:00:00Z","reference_code":"KBX-BATCH-05"}]}'

curl -s -X POST "$BASE_URL/ingest/deposits" \
  -H "Content-Type: application/json" \
  -d '{"deposits":[{"deposit_id":"DEP-2024-010","agent_id":"AGT-150","driver_id":"DRV-112","reference_code":"KBX-BATCH-05","deposited_amount":2650.0,"currency":"KES","deposit_timestamp":"2024-07-15T18:00:00Z"}]}'

echo -e "${GREEN}✓ Scenario 3 data loaded${NC}\n"

# Scenario 4: Missing Deposit
echo -e "${BLUE}=== Scenario 4: Missing Deposit ===${NC}"
curl -s -X POST "$BASE_URL/ingest/orders" \
  -H "Content-Type: application/json" \
  -d '{"orders":[{"order_id":"ORD-2024-020","customer_id":"CUST-20001","expected_amount":2200.0,"currency":"KES","order_created_at":"2024-07-13T08:00:00Z","assigned_driver_id":"DRV-047","status":"DELIVERED_CASH_PENDING","region":"Kenya"}]}'

curl -s -X POST "$BASE_URL/ingest/collections" \
  -H "Content-Type: application/json" \
  -d '{"collections":[{"collection_id":"COL-2024-020","driver_id":"DRV-047","order_ids":["ORD-2024-020"],"collected_amount":2200.0,"currency":"KES","collection_timestamp":"2024-07-13T10:00:00Z","reference_code":"KBX-9020"}]}'

echo -e "${GREEN}✓ Scenario 4 data loaded (no deposit)${NC}\n"

# Scenario 5: Orphaned Deposit
echo -e "${BLUE}=== Scenario 5: Orphaned Deposit ===${NC}"
curl -s -X POST "$BASE_URL/ingest/deposits" \
  -H "Content-Type: application/json" \
  -d '{"deposits":[{"deposit_id":"DEP-2024-030","agent_id":"AGT-239","reference_code":"","deposited_amount":3400.0,"currency":"KES","deposit_timestamp":"2024-07-15T16:00:00Z"}]}'

echo -e "${GREEN}✓ Scenario 5 data loaded (orphaned deposit)${NC}\n"

# Run Reconciliation
echo -e "${BLUE}=== Running Reconciliation Engine ===${NC}"
RUN_RESPONSE=$(curl -s -X POST "$BASE_URL/run" \
  -H "Content-Type: application/json" \
  -d '{"date_range_start":"2024-07-13T00:00:00Z","date_range_end":"2024-07-16T23:59:59Z","region":"Kenya"}')

RUN_ID=$(echo "$RUN_RESPONSE" | jq -r '.runId // empty')

if [ -z "$RUN_ID" ]; then
    echo -e "${YELLOW}Run response:${NC}"
    echo "$RUN_RESPONSE" | jq .
    echo -e "${GREEN}✓ Reconciliation run completed${NC}\n"
else
    echo -e "${GREEN}✓ Reconciliation run started: $RUN_ID${NC}\n"

    # Get Results
    echo -e "${BLUE}=== Fetching Run Details ===${NC}"
    sleep 1
    curl -s "$BASE_URL/run/$RUN_ID" | jq .
fi

echo -e "\n${BLUE}=== Fetching All Reconciliation Results ===${NC}"
curl -s "$BASE_URL/results" | jq 'if type=="array" then .[] | {reconciliation_id, match_quality, confidence_score, status} else . end'

echo -e "\n${GREEN}========================================${NC}"
echo -e "${GREEN}Demo Complete!${NC}"
echo -e "${GREEN}========================================${NC}"
