package com.genkey.healthlogger.model;

import java.util.Map;

/**
 * A health event is an open JSON object. Required keys come from the client
 * spec so a new brief can add {@code region} without a code change.
 */
public record HealthEvent(Map<String, Object> fields) {

    public String status() {
        Object s = fields.get("status");
        return s == null ? null : String.valueOf(s);
    }

    public String service() {
        Object s = fields.get("service");
        return s == null ? null : String.valueOf(s);
    }
}
