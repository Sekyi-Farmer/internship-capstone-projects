package com.genkey.configmaster;

import com.genkey.configmaster.model.ConfigDocument;
import com.genkey.configmaster.model.ValidationIssue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Validates an effective document against types and required fields declared
 * in the client spec — never against hardcoded product fields.
 */
public final class SchemaValidator {

    private SchemaValidator() {}

    public static List<ValidationIssue> validate(ConfigDocument document, ClientSpec spec) {
        List<ValidationIssue> issues = new ArrayList<>();
        Map<String, Object> tree = document.tree();

        if (spec.requiredFields() != null) {
            for (String path : spec.requiredFields()) {
                if (Maps.get(tree, path) == Maps.MISSING) {
                    issues.add(new ValidationIssue(path, "required field is missing"));
                }
            }
        }

        if (spec.fieldTypes() != null) {
            for (var entry : spec.fieldTypes().entrySet()) {
                Object value = Maps.get(tree, entry.getKey());
                if (value == Maps.MISSING || value == null) {
                    continue;
                }
                if (!typeMatches(value, entry.getValue())) {
                    issues.add(new ValidationIssue(
                            entry.getKey(),
                            "expected type " + entry.getValue() + " but got " + value.getClass().getSimpleName()));
                }
            }
        }
        return issues;
    }

    static boolean typeMatches(Object value, String type) {
        return switch (type) {
            case "string" -> value instanceof String;
            case "integer" -> value instanceof Integer || value instanceof Long;
            case "number" -> value instanceof Number;
            case "boolean" -> value instanceof Boolean;
            case "array" -> value instanceof List;
            case "object" -> value instanceof Map;
            default -> true;
        };
    }
}
