package com.genkey.configmaster;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Dotted-path helpers for nested JSON objects. */
public final class Maps {

    /** Sentinel when a path is absent (distinct from JSON null). */
    public static final Object MISSING = new Object();

    private Maps() {}

    @SuppressWarnings("unchecked")
    public static Object get(Map<String, Object> tree, String dottedPath) {
        String[] parts = dottedPath.split("\\.");
        Object current = tree;
        for (String part : parts) {
            if (!(current instanceof Map<?, ?> map) || !map.containsKey(part)) {
                return MISSING;
            }
            current = ((Map<String, Object>) map).get(part);
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> deepCopy(Map<String, Object> source) {
        Map<String, Object> copy = new LinkedHashMap<>();
        for (var e : source.entrySet()) {
            Object v = e.getValue();
            if (v instanceof Map<?, ?> nested) {
                copy.put(e.getKey(), deepCopy((Map<String, Object>) nested));
            } else if (v instanceof List<?> list) {
                copy.put(e.getKey(), new ArrayList<>(list));
            } else {
                copy.put(e.getKey(), v);
            }
        }
        return copy;
    }
}
