#!/usr/bin/env bash

set -e

N="${1:-100}"

PORT=8088
STORE="data/benchmark-events.jsonl"
SERVER_LOG="data/benchmark-server.log"

# Find Maven from PATH, or allow a custom command through MAVEN.
MAVEN="${MAVEN:-mvn}"

if ! command -v "$MAVEN" >/dev/null 2>&1; then
    echo "Maven not found. Install Maven or set MAVEN=/path/to/mvn." >&2
    exit 2
fi

rm -f "$STORE" "$SERVER_LOG"

echo "Starting HealthLogger server..."

"$MAVEN" -q exec:java \
    "-Dexec.mainClass=com.genkey.healthlogger.HealthLoggerCli" \
    "-Dexec.args=serve --store $STORE" \
    > "$SERVER_LOG" 2>&1 &

SERVER_PID=$!

cleanup() {
    kill "$SERVER_PID" 2>/dev/null || true
}

trap cleanup EXIT

echo "Waiting for server..."

SERVER_READY=false

for i in {1..20}; do
    if curl -s "http://127.0.0.1:$PORT/health" > /dev/null; then
        SERVER_READY=true
        break
    fi

    sleep 0.5
done

if [ "$SERVER_READY" != true ]; then
    echo "Server failed to start. Check $SERVER_LOG for details." >&2
    exit 1
fi

echo "Running benchmark with $N events..."

START=$(date +%s%N)

for ((i=1; i<=N; i++)); do
    curl -s -o /dev/null \
        -X POST \
        -H "Content-Type: application/json" \
        -d "{\"service\":\"benchmark-api\",\"status\":\"ok\",\"timestamp\":\"2026-10-06T00:00:00Z\",\"id\":$i}" \
        "http://127.0.0.1:$PORT/events"
done

END=$(date +%s%N)

ELAPSED_NS=$((END - START))

ELAPSED_SECONDS=$(awk \
    "BEGIN {printf \"%.3f\", $ELAPSED_NS / 1000000000}")

EVENTS_PER_SECOND=$(awk \
    "BEGIN {printf \"%.2f\", $N / ($ELAPSED_NS / 1000000000)}")

echo
echo "Benchmark complete."
echo "Events:           $N"
echo "Elapsed time:     ${ELAPSED_SECONDS} seconds"
echo "Events/second:    $EVENTS_PER_SECOND"
