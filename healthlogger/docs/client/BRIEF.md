# Client brief — Harbor Ops (sample)

> Mentors may replace this letter. Week 1 `docs/SPEC.md` must follow the
> current brief, not the sample service names below.

**From:** Jordan Adeyemi, SRE, Harbor Logistics  
**To:** Intern engineering cohort  
**Re:** Local health and event logger

Our laptops and a small lab rack have no central observability yet. We need a
**local** tool (CLI first, tiny HTTP second) that:

1. Records health events as **one JSON object per line** on disk (`data/` is
   fine). Do not use a remote database in v1.
2. Required fields and allowed `status` values will be supplied as a JSON spec
   we can edit. Today we care about `service`, `status`, and `timestamp`.
   Statuses: `ok`, `degraded`, `down`. Map those to log severities
   (`info` / `warn` / `error`) for stdout.
3. CLI: `record` (append an event), `query` (filter by service), `serve`
   (HTTP on localhost).
4. HTTP: `POST /events` with a JSON body; `GET /health` returns process liveness
   only (not a full status page).
5. Structured logs to stdout as JSON lines so we can ship them later.

Out of scope for v1: authentication (unless a later brief asks), TLS,
multi-node, Prometheus exporters, a browser UI.

If we add a field such as `region`, we will send a new spec file — the logger
must not require a Java change for a new required field name.
