"""Load client-spec.json. Statuses, roles, and tokens are data — not enums."""

from __future__ import annotations

import json
import os
from pathlib import Path
from typing import Any


def spec_path() -> Path:
    env = os.environ.get("ALERTDESK_SPEC")
    if env:
        return Path(env)
    return Path("config/client-spec.json")


def load_spec(path: Path | None = None) -> dict[str, Any]:
    p = path or spec_path()
    with p.open(encoding="utf-8") as f:
        return json.load(f)


def can_transition(spec: dict[str, Any], current: str, target: str) -> bool:
    allowed = spec.get("transitions", {}).get(current, [])
    return target in allowed


def role_for_token(spec: dict[str, Any], token: str) -> str | None:
    return spec.get("tokens", {}).get(token)


def role_allows(spec: dict[str, Any], role: str, action: str) -> bool:
    return action in spec.get("roles", {}).get(role, [])
