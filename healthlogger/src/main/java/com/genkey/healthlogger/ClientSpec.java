package com.genkey.healthlogger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/** Runtime client needs. New required fields are data, not a new Java class. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientSpec(
        String schemaVersion,
        String client,
        List<String> requiredFields,
        List<String> allowedStatuses,
        Map<String, String> severityMap,
        Integer httpPort
) {
    public int port() {
        return httpPort == null ? 8088 : httpPort;
    }

    public String severityFor(String status) {
        if (severityMap == null) {
            return "info";
        }
        return severityMap.getOrDefault(status, "info");
    }
}
