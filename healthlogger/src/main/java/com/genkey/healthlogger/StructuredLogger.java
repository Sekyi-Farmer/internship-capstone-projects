package com.genkey.healthlogger;

import com.genkey.healthlogger.model.HealthEvent;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** JSON-line logs to stdout (not mixed with query JSON). */
public final class StructuredLogger {

    private StructuredLogger() {}

    public static void event(ClientSpec spec, HealthEvent event) {
        Map<String, Object> line = new LinkedHashMap<>();
        line.put("ts", Instant.now().toString());
        line.put("severity", spec.severityFor(event.status()));
        line.put("event", event.fields());
        try {
            System.out.println(Json.toJson(line));
        } catch (Exception e) {
            System.err.println("{\"severity\":\"error\",\"message\":\"log serialize failed\"}");
        }
    }
}
