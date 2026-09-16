"""Stub bearer auth. Tokens and roles come from the client spec (Month 4: replace)."""

from __future__ import annotations

from typing import Any

from fastapi import Depends, Header, HTTPException

from .spec import load_spec, role_allows, role_for_token


def current_actor(
    authorization: str | None = Header(default=None),
) -> dict[str, Any]:
    spec = load_spec()
    if not authorization or not authorization.lower().startswith("bearer "):
        raise HTTPException(status_code=401, detail="Bearer token required")
    token = authorization.split(" ", 1)[1].strip()
    role = role_for_token(spec, token)
    if role is None:
        raise HTTPException(status_code=401, detail="Unknown token")
    return {"token": token, "role": role, "spec": spec}


def require(action: str):
    def _dep(actor: dict[str, Any] = Depends(current_actor)) -> dict[str, Any]:
        if not role_allows(actor["spec"], actor["role"], action):
            raise HTTPException(status_code=403, detail=f"role {actor['role']} cannot {action}")
        return actor

    return _dep
