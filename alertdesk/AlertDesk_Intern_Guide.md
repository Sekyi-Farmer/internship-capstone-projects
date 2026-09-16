# AlertDesk
Year 2 Cybersecurity Capstone · Intern Step-by-Step Build Guide · 8 weeks · Python 3.12 · FastAPI

This is a **production-style web service** (Months 4–6 of the shared curriculum). The scaffold already serves tickets; you specify it from the **client**, test it, then harden auth, observability, and deploy.

## Overview

Tickets, audit rows, and stub bearer tokens. Legal status moves and who may perform them live in `config/client-spec.json`.

## Tech stack

Python 3.12, FastAPI, SQLite, pytest, Semgrep, Trivy after Docker.

---

## Weeks 1–4 (API, schema, security)

### Week 1 — Spec from the client

Read `alertdesk/`. Draw: HTTP → auth stub → spec checks → SQLite → audit.

Read [docs/client/BRIEF.md](docs/client/BRIEF.md). Write `docs/SPEC.md` (purpose, API resources, acceptance criteria) and a short **threat/abuse** note: spoofed tokens, illegal transitions, information in audit logs.

**Client:** If the SOC workflow changes, update spec JSON first, then tests — not hardcoded status strings.

**Deliverable:** `docs/SPEC.md`.

### Week 2 — Secure Git

Fork `YOUR_ORG/alertdesk`. Hybrid PRs as in LIDAS. Complete `SECURITY.md` (stub tokens are lab-only). Secret scanning on.

### Week 3 — Tests

Finish `tests/test_auth.py`. Fuzz create bodies. Coverage ≥80%; uncomment `fail_under` in `pyproject.toml`.

### Week 4 — OpenAPI and assessment

Export or commit OpenAPI (`/openapi.json`). Write a security assessment vs OWASP for the **current** stub. Month 4 milestone PR.

## Weeks 5–8 (ops)

### Week 5

`scripts/benchmark.py` for create/list latency. Add structured logging or a metrics hook as the brief allows. `docs/BENCHMARKS.md`.

### Week 6

`docs/ARCHITECTURE.md`. One client-driven extension: new status in spec, feature flag for a breaking workflow, or contract tests.

### Week 7

Non-root Dockerfile, compose with a volume for SQLite (or document why you moved off SQLite). Trivy in CI. Sketch a migration if you add a column.

### Week 8

Incident playbook, CHANGELOG, v1.0.0, handoff to `intern/YOUR_USERNAME`.

## Evaluation

Spec vs brief, transition correctness, authz tests, OWASP notes, CI, production hygiene — not a full SOAR product.
