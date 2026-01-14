#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

# 1) Build all Java code (unit + component tests)
./mvnw clean test

# 2) Build all Docker images defined in the root compose file
docker compose -f docker-compose.yml build

# 3) Start the full system
docker compose -f docker-compose.yml up -d

# 4) Run end-to-end tests (Cucumber/JUnit)
./mvnw -pl end-to-end-tests test

# 5) Stop the system (recommended for CI)
docker compose -f docker-compose.yml down

