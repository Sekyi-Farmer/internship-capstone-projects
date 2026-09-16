package com.genkey.configmaster.model;

import java.util.Map;

/**
 * A loaded configuration document. The tree is a JSON object (nested maps,
 * lists, and scalars). Behaviour (required fields, merge rules) lives in
 * {@code client-spec.json}, not in this type — so a new client can change
 * the schema without a new Java class per field.
 */
public record ConfigDocument(Map<String, Object> tree) {}
