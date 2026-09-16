"""Week 3 deliverable: auth, assign, list, audit, fuzz.

TODO (Week 3):
- analyst cannot transition (403)
- unknown token (401)
- assign then legal triaged transition
- severity not in spec (400)
- malformed JSON / extra fields
"""

import pytest


@pytest.mark.skip(reason="Week 3 — implement analyst-cannot-transition")
def test_analyst_cannot_transition():
    pass


@pytest.mark.skip(reason="Week 3 — implement unknown bearer token")
def test_unknown_token_is_401():
    pass
