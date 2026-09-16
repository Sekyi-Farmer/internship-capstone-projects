package com.genkey.healthlogger;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Week 3 deliverable: HTTP and fuzz tests.
 *
 * TODO (Week 3):
 * - GET /health returns 200
 * - POST /events happy path and 400 on missing fields
 * - POST with extra fields from a new client spec still stores them
 * - truncated JSON, empty body, huge payload
 */
class HealthHttpServerTest {

    @Test
    @Disabled("Week 3 — start server on an ephemeral port and POST /events")
    void postEventsCreatesRecord() {
        // write this
    }

    @Test
    @Disabled("Week 3 — missing required field returns 400")
    void postEventsRejectsIncompleteBody() {
        // write this
    }
}
