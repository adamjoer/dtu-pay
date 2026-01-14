#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

./mvnw -pl facade -am clean test
./mvnw -pl facade -am package -DskipTests

docker compose -f docker-compose.yml build facade


