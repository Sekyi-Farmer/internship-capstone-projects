# GenKey intern capstones

This is the **main capstone repository**. Each intern picks **one** project
subdirectory and works there. Shared git workflow (fork, weekly PRs to your
fork `main`, milestones to upstream `intern/YOUR_USERNAME`) applies to this
repo as a whole.

| Directory | Year | Specialty | Stack |
|-----------|------|-----------|--------|
| [lidas](lidas/) | 1 | Cybersecurity | Python CLI |
| [configmaster](configmaster/) | 1 | Software Engineering | Java 21 CLI |
| [healthlogger](healthlogger/) | 1 | Software Engineering | Java 21 CLI + local HTTP |
| [alertdesk](alertdesk/) | 2 | Cybersecurity | Python FastAPI |

`lidas-mentor-solutions/` is mentor-only reference material. Do not copy it
into intern forks as “the answers.”

## Quick start

```bash
git clone https://github.com/YOUR_USERNAME/capstone.git
cd capstone/<project>
```

Follow that project’s `README.md` and intern guide. GitHub Actions at the
repo root run only when files under that project change.

## Layout

```
capstone/
├── lidas/
├── configmaster/
├── healthlogger/
├── alertdesk/
└── .github/workflows/   # monorepo CI (path-filtered)
```
