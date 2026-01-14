#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

./mvnw -pl messaging-utilities -am clean test
./mvnw -pl messaging-utilities -am package -DskipTests
