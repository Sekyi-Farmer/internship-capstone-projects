# ConfigMaster
Year 1 Software Engineering Capstone · Intern Step-by-Step Build Guide · 8 weeks · Java 21

This guide walks you from the provided scaffold to a tested, versioned, containerised CLI. Code scaffolding is already here — read, extend, and test it. Do not skip Week 1: the **client brief** is the contract, not this README.

Tasks that change when a mentor drops a new client letter are marked **Client**. Treat them as first-class work.

## Project overview

ConfigMaster loads JSON configuration layers, validates the effective tree against `config/client-spec.json`, and prints merged JSON. Merge strategies (`override`, `append_list`, `fail_on_conflict`) are data, not hardcoded field names.

By the end of Month 2 you will have: a working CLI, ≥80% test coverage, architecture docs, a non-root Docker image, and CI (tests + SAST + Trivy).

## Tech stack

- Java 21, Maven, JUnit 5, Jackson
- JaCoCo (coverage gate in Week 3)
- Semgrep in CI; Trivy after Week 7 Dockerfile
- No Spring Boot

## Before you start

Install JDK 21, Maven, Git, Docker Desktop, and a GitHub account.

---

## MONTH 1 — Spec · Secure Repo · Testing · Docs

### Week 1 — Spec from the client

Duration: ~8–10 hours. **Do not change product behaviour until `docs/SPEC.md` exists.**

**Day 1–2 · Read the scaffold**

- Read every class under `src/main/java/com/genkey/configmaster/`
- Draw the flow: files → Loader → Merger → SchemaValidator → stdout/stderr
- Read [docs/client/BRIEF.md](docs/client/BRIEF.md) and [docs/client/spec.example.json](docs/client/spec.example.json)

**Day 2–5 · Write the spec**

Create `docs/SPEC.md` with:

- Purpose (one paragraph in the client’s language)
- Scope / out of scope (from the brief)
- Acceptance criteria for validate, merge, show
- How a **new client spec JSON** would be adopted (file path, who owns it)

**Client:** If your mentor replaces `BRIEF.md`, rewrite `SPEC.md` to match. Do not keep Northwind fields if the new client does not have them.

**Deliverable:** `docs/SPEC.md` committed.

### Week 2 — Secure Git setup

Fork `YOUR_ORG/configmaster`, clone your fork, optional `upstream` remote.

**PRs:** weekly → fork `main`. Milestones → upstream `intern/YOUR_USERNAME`. Never upstream `main`.

Enable branch protection on `main`, secret scanning + push protection, gitleaks pre-commit if your mentor requires it. Complete `SECURITY.md` placeholders. Confirm `data/*` is gitignored except `.gitkeep`.

**Deliverable:** hardened fork, completed `SECURITY.md`.

### Week 3 — Tests

Enable the disabled tests in `SchemaValidatorTest`. Add positive, negative, and malformed-JSON cases. Target ≥80% line coverage, then uncomment the JaCoCo `check` execution in `pom.xml`.

**Deliverable:** full suite green; coverage gate on.

### Week 4 — Docs and Month 1 PR

Document the spec JSON schema in `docs/schema.md` (requiredFields, fieldTypes, mergeStrategies). PR to fork `main`, then milestone PR to upstream `intern/YOU`.

---

## MONTH 2 — Architecture · CI · Docker

### Week 5 — Benchmarks

Create `scripts/benchmark.sh` that merges a large generated JSON tree and records wall time in `docs/BENCHMARKS.md`.

### Week 6 — Architecture + client extension

Write `docs/ARCHITECTURE.md` (components, trust boundaries, how plugins/strategies work). Implement **one** client-driven extension, for example:

- YAML loader
- a new merge strategy named in an updated `client-spec.json`
- semantic version check of `schemaVersion`

**Client:** pick the extension that the current brief actually needs.

### Week 7 — Docker and CI

Hardened image: non-root user, no secrets in layers, `ENTRYPOINT` the CLI. Extend CI so Trivy runs. Document compose if you mount `fixtures/` read-only.

### Week 8 — Release

`CHANGELOG.md`, version `1.0.0`, GitHub release on your fork, handoff PR to `intern/YOUR_USERNAME`.

## Evaluation (end of Month 2)

Spec quality vs the brief, tests (including fuzz), merge correctness, architecture, CI blocking, extension quality, changelog.
