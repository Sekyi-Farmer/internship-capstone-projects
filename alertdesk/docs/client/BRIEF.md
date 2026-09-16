# Client brief — Sentinel SOC (sample)

> Mentors may replace this letter. `docs/SPEC.md` and `config/client-spec.json`
> must follow the current client, not these sample status names.

**From:** Amira Haddad, SOC Manager, Sentinel Credit Union  
**To:** Intern engineering cohort  
**Re:** Alert and ticket intake API

We need a **service** (not a CLI-only tool) that analysts use to file and
progress security alerts.

v1 must:

1. Create a ticket with title, description, and a **severity** from a list we
   control (today: low, medium, high, critical).
2. Tickets move through **statuses** we define. Today: new → triaged →
   assigned → resolved → closed. Not every jump is legal (for example new
   cannot skip to resolved). Put legal transitions in a JSON spec so we can
   tighten workflow without a rewrite.
3. **Assign** a ticket to an analyst identifier (email or handle).
4. Access control: analysts can create/list/get/assign; only leads and admins
   may change status. Use bearer tokens for the scaffold; you will replace
   this with real auth in Month 4.
5. Append-only **audit** of who did what (create, assign, transition).
6. OpenAPI from the running app.

Out of scope for the scaffold: Slack, SIEM connectors, SSO, multi-region.

If we add a status `waiting-vendor`, we will send an updated spec JSON —
statuses and roles must not be hardcoded enums in Python.
