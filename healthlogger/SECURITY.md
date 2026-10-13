# Security Policy

## Overview

HealthLogger is a local service-health and event logging application developed as part of the GenKey Software Engineering Internship for Harbor Logistics.

This policy describes vulnerability reporting, runtime data handling, network exposure, secure development practices, and the limitations of the current implementation.

The policy applies to the current HealthLogger implementation and should be reviewed when the client requirements or deployment model change.

## Reporting a Vulnerability

Report suspected security vulnerabilities privately to the project maintainer rather than disclosing them publicly.

Use the repository's private vulnerability reporting feature if it is enabled. Otherwise, ask the project supervisor or repository maintainer for the approved private reporting contact. Do not assume a reporting email address unless it has been officially provided.

A vulnerability report should include:

- A clear description of the security issue and its potential impact.
- The affected component, file, or feature, if known.
- Steps to reproduce the issue, where safe to do so.
- Any suggested mitigation or fix, if available.

Do not include passwords, access tokens, API keys, real credentials, or other sensitive operational data in a report. Remove sensitive information from logs and screenshots before sharing them.

Vulnerabilities should not be publicly disclosed through GitHub issues, pull requests, or other public project channels before the maintainers have had a reasonable opportunity to investigate.

The reporting channel and acknowledgement process must be confirmed with the project supervisor or repository maintainer.

## Event Store

HealthLogger stores runtime events as JSON Lines (JSONL) files. The default event store is:

`data/events.jsonl`

The event store location can also be configured through the `HEALTHLOGGER_STORE` environment variable or the applicable CLI `--store` option.

The rejected-event store may contain request metadata and validation reasons for rejected HTTP submissions. Treat accepted and rejected event files, along with runtime logs, as potentially sensitive operational data.

- Do not commit runtime event files, logs, or real operational data to Git.
- Keep runtime data local and restrict access to authorized users.
- Use synthetic or sanitized data for tests and examples.
- Do not commit passwords, access tokens, API keys, credentials, or other secrets.
- Review files before committing changes.
- Preserve the repository's intended `.gitignore` rules for runtime data.

If a credential or sensitive value is committed accidentally, revoke or rotate the affected credential promptly. Removing it from the latest file or commit does not remove it from Git history. Coordinate any history cleanup with repository maintainers, especially if the branch has already been shared.

## HTTP Bind Address

The HealthLogger HTTP server binds to the loopback address `127.0.0.1` by default. This limits access to the local machine, but it is not a substitute for authentication or other security controls.

The current client specification does not require authentication or TLS for the local application.

Do not expose the server to a public or untrusted network without an explicit requirement and an appropriate security review. Any change to the bind address or deployment model must consider authentication, authorization, transport security, request-size limits, input validation, logging, configuration, and access to stored data.

New security requirements should be documented in the project specification and reflected in implementation and testing.

## Scope and Limitations

HealthLogger is a local service-health event logging and query application. It is not:

- A Security Information and Event Management (SIEM) platform.
- A multi-tenant Software-as-a-Service (SaaS) application.
- A centralized remote observability platform.
- A replacement for enterprise security monitoring.
- A publicly exposed production API by default.
- A healthcare information system.

The current implementation is designed around the local application requirements in the Harbor Logistics client specification. It does not provide every security control that might be required for an internet-facing or enterprise deployment.

Future client requirements must be reviewed and documented before expanding the application's scope or deployment model.

## Secure Development Practices

Development of HealthLogger should follow basic secure software development practices:

- Validate user-provided input before processing or storing it.
- Avoid hard-coded passwords, API keys, tokens, and other secrets.
- Keep runtime and sensitive data outside version control.
- Use synthetic data during development and testing.
- Review changes before committing them.
- Write tests for security-relevant validation rules.
- Record relevant security-related events through appropriate application logging.
- Keep dependencies and development tools reasonably up to date.
- Document new security requirements when the application scope changes.

## Security and Testing

Security-related requirements should be verified through testing where applicable.

Testing should include:

- Invalid and unexpected input.
- Boundary values.
- Missing and null required fields.
- Validation of data before storage.
- Verification that the HTTP server uses the intended loopback address.
- Verification that sensitive runtime files are not committed to Git.
- Verification that application configuration does not expose secrets.

Security tests should use synthetic or non-sensitive information.

## Changes to This Policy

This policy should be updated when the HealthLogger architecture, deployment model, data-handling requirements, or client security requirements change.

Significant security changes should be documented, tested, and reviewed before being incorporated into the project.
