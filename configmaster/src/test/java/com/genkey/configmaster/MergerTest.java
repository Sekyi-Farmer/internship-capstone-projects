package com.genkey.configmaster;

import com.genkey.configmaster.model.ConfigDocument;
import com.genkey.configmaster.model.MergeResult;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Starter merge tests — extend in Week 3 (every strategy, nested maps, empty files). */
class MergerTest {

    private final ClientSpec spec;

    MergerTest() throws Exception {
        spec = Loader.loadSpec(Path.of("config/client-spec.json"));
    }

    @Test
    void overlayPortWinsAndFeaturesAppend() throws Exception {
        ConfigDocument defaults = Loader.loadJson(Path.of("fixtures/defaults.json"));
        ConfigDocument overlay = Loader.loadJson(Path.of("fixtures/overlay.json"));
        MergeResult result = Merger.merge(List.of(defaults, overlay), spec);
        assertTrue(result.ok());
        @SuppressWarnings("unchecked")
        Map<String, Object> app = (Map<String, Object>) result.document().tree().get("app");
        assertEquals(9090, app.get("port"));
        assertEquals(List.of("metrics", "tracing"), app.get("features"));
    }

    @Test
    void regionConflictFails() throws Exception {
        ConfigDocument defaults = Loader.loadJson(Path.of("fixtures/defaults.json"));
        ConfigDocument overlay = Loader.loadJson(Path.of("fixtures/conflict-overlay.json"));
        MergeResult result = Merger.merge(List.of(defaults, overlay), spec);
        assertEquals(1, result.issues().size());
        assertEquals("app.region", result.issues().getFirst().path());
    }
}
