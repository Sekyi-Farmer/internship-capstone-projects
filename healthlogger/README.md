# HealthLogger API

Intern capstone scaffold for the **HealthLogger Intern Guide** (Year 1, 8 weeks).
Follow the guide week by week; documentation deliverables are not pre-written.

![CI](https://github.com/YOUR_USERNAME/capstone/actions/workflows/healthlogger.yml/badge.svg)

## What it does

HealthLogger records health events as JSON lines on disk, logs structured JSON
to stdout, and optionally serves `POST /events` and `GET /health` on localhost
using the JDK HTTP server (not Spring). Required fields and statuses come from
`config/client-spec.json` so a new client brief can change the schema.

## Quick start

```bash
git clone https://github.com/YOUR_USERNAME/capstone.git
cd capstone/healthlogger
mvn test
mvn -q exec:java "-Dexec.arguments=record,--json-file,fixtures/one-event.json"
mvn -q exec:java "-Dexec.arguments=query,--service,payments-api"
```

Use `exec.arguments` (comma-separated) so Maven does not steal flags such as `--file`. Inline JSON is optional: `record '{"service":"..."}'`.

`serve` listens on `127.0.0.1` and the port in the client spec (default 8088).

## Git / PR workflow

| Work | PR target |
|------|-----------|
| Weekly | Fork **`main`** |
| Milestones | Upstream **`intern/YOUR_USERNAME`** |
| Never | Upstream **`main`** |

## CLI commands

- **record** — validate and append one JSON event
- **query** — print stored events (`--service` filter)
- **serve** — JDK HTTP server

## Client flexibility

| File | Role |
|------|------|
| [docs/client/BRIEF.md](docs/client/BRIEF.md) | Sample Harbor Ops letter |
| [docs/client/spec.example.json](docs/client/spec.example.json) | Example spec |
| [config/client-spec.json](config/client-spec.json) | Runtime spec |
| `docs/SPEC.md` | You write this in Week 1 |

## Internship path

| Week | You create / complete |
|------|------------------------|
| 1 | `docs/SPEC.md` from the client brief |
| 2 | Fork, `SECURITY.md`, secret scanning |
| 3 | HTTP tests; ≥80% coverage; JaCoCo check |
| 4 | `docs/log-schema.md`; Month 1 PR |
| 5 | `scripts/benchmark.sh` + `docs/BENCHMARKS.md` |
| 6 | `docs/ARCHITECTURE.md`; auth header or new event type from the brief |
| 7 | Dockerfile (non-root) + Trivy |
| 8 | CHANGELOG, v1.0.0, handoff |

See **HealthLogger_Intern_Guide.md**.
