# Client brief — Northwind Platform (sample)

> Mentors may replace this letter with a different client. Treat it as the
> source of truth for Week 1 `docs/SPEC.md`. Do not assume the README is the
> contract — the client is.

**From:** Priya Shah, Platform Lead, Northwind Payments  
**To:** Intern engineering cohort  
**Re:** One CLI for layered configuration

We ship the same service to three environments. Today people copy JSON files
by hand and we keep finding missing keys in production.

We need a **command-line tool** (not a web console) that:

1. Loads one or more JSON config files in order (defaults, then environment
   overlay, then a laptop override).
2. Validates the **effective** document against a schema we will give you
   (required fields and types). The schema will change when product needs
   change — do not hard-code field names in Java.
3. Merges objects deeply. For lists of feature flags we want **append**, not
   replace. If two layers set `app.region` to different values, we want a
   **hard failure** (we cannot silently pick one). Everything else: later
   layer wins.
4. Prints the merged JSON to stdout (`show`) so we can pipe it into deploy
   scripts. Exit non-zero on validation or merge conflict.
5. Logs what it did (files loaded, issue count) to stderr so stdout stays
   pure JSON.

Out of scope for v1: YAML, remote HTTP config, encryption of secrets, a GUI.

We will send an updated schema JSON if we add fields. Your tool should read
that file at runtime (`config/client-spec.json`).
