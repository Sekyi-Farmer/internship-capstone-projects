package com.genkey.configmaster;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkey.configmaster.model.ConfigDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads JSON files into maps. YAML is intentionally absent — Week 6
 * intern/client extension.
 */
public final class Loader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Loader() {}

    public static ConfigDocument loadJson(Path path) throws IOException {
        try (var in = Files.newInputStream(path)) {
            Map<String, Object> tree = MAPPER.readValue(in, new TypeReference<>() {});
            if (tree == null) {
                tree = new LinkedHashMap<>();
            }
            return new ConfigDocument(tree);
        }
    }

    public static ClientSpec loadSpec(Path path) throws IOException {
        try (var in = Files.newInputStream(path)) {
            return MAPPER.readValue(in, ClientSpec.class);
        }
    }

    public static String toJson(Object value) throws IOException {
        return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
    }
}
