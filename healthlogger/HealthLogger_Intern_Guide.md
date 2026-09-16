# HealthLogger
Year 1 Software Engineering Capstone · Intern Step-by-Step Build Guide · 8 weeks · Java 21

Scaffold is provided. Your job is to understand it, specify it from the **client brief**, test it, and extend it when the client changes.

## Overview

HealthLogger is a local JSONL event store plus a tiny HTTP API. Field names live in `client-spec.json`, not in hardcoded Java beans.

## Tech stack

Java 21, Maven, JUnit 5, Jackson, JDK `HttpServer`. No Spring Boot.

---

## Month 1

### Week 1 — Spec from the client

Read all sources under `src/main/java`. Draw: CLI/HTTP → validate → JsonlStore → stdout log.

Read [docs/client/BRIEF.md](docs/client/BRIEF.md). Create `docs/SPEC.md`: purpose, scope, acceptance criteria for `record` / `query` / `serve`, and how a new required field is added **only** by editing the spec JSON.

**Client:** If the brief is replaced, rewrite the spec. Do not keep Harbor status names if the new client uses different ones.

**Deliverable:** `docs/SPEC.md`.

### Week 2 — Secure Git

Fork `YOUR_ORG/healthlogger`. Weekly PRs to fork `main`; milestones to upstream `intern/YOUR_USERNAME`. Complete `SECURITY.md`. Enable secret scanning. Runtime `data/*.jsonl` must stay untracked.

### Week 3 — Tests

Implement `HealthHttpServerTest`. Cover missing fields, extra fields, malformed JSON. Uncomment JaCoCo ≥80% check.

### Week 4 — Log schema docs

`docs/log-schema.md` describing JSONL and stdout log shape. Month 1 milestone PR.

## Month 2

### Week 5

`scripts/benchmark.sh` appending N events; record results in `docs/BENCHMARKS.md`.

### Week 6

`docs/ARCHITECTURE.md`. One client-driven extension: e.g. optional `Authorization` header from spec, retention, or HMAC of the store if the brief asks.

### Week 7

Non-root Dockerfile; compose mounting `data/` as a volume; Trivy in CI.

### Week 8

v1.0.0, CHANGELOG, handoff PR.

## Evaluation

Spec vs brief, tests (fuzz), store integrity, architecture, CI, extension quality.
