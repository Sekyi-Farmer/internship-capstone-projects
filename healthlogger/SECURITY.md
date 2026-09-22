# Security Policy

## Reporting a vulnerability

If a security vulnerability is discovered in HealthLogger, it should be
reported privately to the project maintainers rather than being disclosed
publicly first.

A vulnerability report should include:

* A clear description of the issue.
* The affected component or file, if known.
* Steps to reproduce the issue.
* The potential security impact.
* Any suggested mitigation, if available.

Do not include real credentials, secrets, personal information, or other
sensitive operational data in a vulnerability report.

## Event store

HealthLogger stores runtime events as JSON Lines (JSONL) in the `data/`
directory. The default event store is:

`data/events.jsonl`

The store location can also be configured using the `HEALTHLOGGER_STORE`
environment variable.

Runtime event files may contain operational information about services and
their health status. They must therefore be treated as operational data and
must not be committed to Git.

The repository `.gitignore` excludes runtime files under `data/` while
allowing the directory placeholder to remain tracked.

When testing HealthLogger, use sample or synthetic data rather than real
sensitive operational information.

## HTTP bind address

The HealthLogger HTTP server binds to the loopback address:

`127.0.0.1`

This means the HTTP API is intended to be accessible only from the local
machine by default. It is not intended to provide a publicly exposed network
service.

The current client brief does not require authentication or TLS. If a future
client brief requires network exposure, authentication, TLS, or another
security control, the specification and implementation must be updated
accordingly.

## Scope limitations

HealthLogger is a local health and event logging tool. It is not intended to
be:

* A Security Information and Event Management (SIEM) platform.
* A multi-tenant SaaS application.
* A centralized remote observability platform.
* A replacement for enterprise security monitoring.
* A publicly exposed production API by default.

Security controls should therefore be considered within the scope of the
current local application and its client requirements.

Future security requirements introduced by a client brief must be documented
in the project specification before implementation.
