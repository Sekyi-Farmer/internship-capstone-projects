package com.genkey.configmaster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

/**
 * Machine-readable client needs. Loaded at runtime so a mentor can drop a
 * new brief + spec JSON without rewriting the engine.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ClientSpec(
        String schemaVersion,
        String client,
        List<String> requiredFields,
        Map<String, String> fieldTypes,
        Map<String, String> mergeStrategies,
        String defaultMergeStrategy
) {
    public String strategyFor(String dottedPath) {
        if (mergeStrategies != null && mergeStrategies.containsKey(dottedPath)) {
            return mergeStrategies.get(dottedPath);
        }
        return defaultMergeStrategy == null ? "override" : defaultMergeStrategy;
    }
}
