package com.genkey.configmaster;

import com.genkey.configmaster.model.ConfigDocument;
import com.genkey.configmaster.model.MergeResult;
import com.genkey.configmaster.model.ValidationIssue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Deep-merges configuration layers. Per-path strategies come from the client
 * spec: {@code override}, {@code append_list}, {@code fail_on_conflict}.
 */
public final class Merger {

    private Merger() {}

    public static MergeResult merge(List<ConfigDocument> layers, ClientSpec spec) {
        if (layers.isEmpty()) {
            return new MergeResult(new ConfigDocument(new LinkedHashMap<>()), List.of());
        }
        Map<String, Object> acc = Maps.deepCopy(layers.getFirst().tree());
        List<ValidationIssue> issues = new ArrayList<>();
        for (int i = 1; i < layers.size(); i++) {
            mergeInto(acc, layers.get(i).tree(), "", spec, issues);
        }
        return new MergeResult(new ConfigDocument(acc), issues);
    }

    @SuppressWarnings("unchecked")
    private static void mergeInto(
            Map<String, Object> base,
            Map<String, Object> overlay,
            String prefix,
            ClientSpec spec,
            List<ValidationIssue> issues) {
        for (var e : overlay.entrySet()) {
            String path = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
            Object incoming = e.getValue();
            if (!base.containsKey(e.getKey())) {
                base.put(e.getKey(), copyValue(incoming));
                continue;
            }
            Object existing = base.get(e.getKey());
            String strategy = spec.strategyFor(path);

            if (existing instanceof Map<?, ?> && incoming instanceof Map<?, ?>) {
                mergeInto(
                        (Map<String, Object>) existing,
                        (Map<String, Object>) incoming,
                        path,
                        spec,
                        issues);
                continue;
            }

            switch (strategy) {
                case "append_list" -> {
                    if (existing instanceof List<?> a && incoming instanceof List<?> b) {
                        List<Object> combined = new ArrayList<>(a);
                        combined.addAll(b);
                        base.put(e.getKey(), combined);
                    } else {
                        issues.add(new ValidationIssue(path, "append_list requires arrays on both sides"));
                    }
                }
                case "fail_on_conflict" -> {
                    if (!Objects.equals(existing, incoming)) {
                        issues.add(new ValidationIssue(
                                path,
                                "conflict: " + existing + " vs " + incoming));
                    }
                }
                default -> base.put(e.getKey(), copyValue(incoming));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static Object copyValue(Object value) {
        if (value instanceof Map<?, ?> map) {
            return Maps.deepCopy((Map<String, Object>) map);
        }
        if (value instanceof List<?> list) {
            return new ArrayList<>(list);
        }
        return value;
    }
}
