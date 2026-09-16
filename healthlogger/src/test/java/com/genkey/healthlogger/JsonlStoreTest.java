package com.genkey.healthlogger;

import com.genkey.healthlogger.model.HealthEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Starter store tests — extend in Week 3. */
class JsonlStoreTest {

    @TempDir
    Path temp;

    @Test
    void appendThenQueryByService() throws Exception {
        JsonlStore store = new JsonlStore(temp.resolve("events.jsonl"));
        store.append(new HealthEvent(Map.of(
                "service", "payments-api",
                "status", "ok",
                "timestamp", "2026-09-15T12:00:00Z")));
        store.append(new HealthEvent(Map.of(
                "service", "edge-proxy",
                "status", "down",
                "timestamp", "2026-09-15T12:01:00Z")));
        List<HealthEvent> found = store.query("payments-api");
        assertEquals(1, found.size());
        assertEquals("ok", found.getFirst().status());
    }

    @Test
    void validateRejectsUnknownStatus() throws Exception {
        ClientSpec spec = Json.loadSpec(Path.of("config/client-spec.json"));
        HealthEvent event = new HealthEvent(Map.of(
                "service", "x",
                "status", "on-fire",
                "timestamp", "2026-09-15T12:00:00Z"));
        List<String> issues = Json.validate(event, spec);
        assertTrue(issues.stream().anyMatch(s -> s.contains("status not allowed")));
    }
}
