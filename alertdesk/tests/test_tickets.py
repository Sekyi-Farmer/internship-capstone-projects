"""Starter API tests — one happy path and one illegal transition."""

from __future__ import annotations

import os

import pytest
from fastapi.testclient import TestClient

from alertdesk import db
from alertdesk.app import app


@pytest.fixture()
def client(tmp_path, monkeypatch):
    dbfile = tmp_path / "test.sqlite"
    monkeypatch.setenv("ALERTDESK_DB", str(dbfile))
    monkeypatch.setenv("ALERTDESK_SPEC", str(os.path.abspath("config/client-spec.json")))
    conn = db.connect(dbfile)
    db.init_schema(conn)
    conn.close()
    with TestClient(app) as c:
        yield c


def test_create_ticket_happy_path(client: TestClient):
    res = client.post(
        "/tickets",
        json={"title": "Phish report", "description": "user clicked", "severity": "high"},
        headers={"Authorization": "Bearer analyst-token"},
    )
    assert res.status_code == 200
    body = res.json()
    assert body["status"] == "new"
    assert body["severity"] == "high"


def test_illegal_transition_new_to_resolved(client: TestClient):
    created = client.post(
        "/tickets",
        json={"title": "Brute force", "severity": "medium"},
        headers={"Authorization": "Bearer lead-token"},
    )
    ticket_id = created.json()["id"]
    res = client.post(
        f"/tickets/{ticket_id}/transition",
        json={"status": "resolved"},
        headers={"Authorization": "Bearer lead-token"},
    )
    assert res.status_code == 409
