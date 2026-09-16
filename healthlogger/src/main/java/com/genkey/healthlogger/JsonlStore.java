package com.genkey.healthlogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genkey.healthlogger.model.HealthEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/** Append-only JSONL store under {@code data/} (runtime files are gitignored). */
public final class JsonlStore {

    private final Path path;
    private final ObjectMapper mapper = Json.MAPPER;

    public JsonlStore(Path path) {
        this.path = path;
    }

    public void append(HealthEvent event) throws IOException {
        Files.createDirectories(path.getParent());
        String line = mapper.writeValueAsString(event.fields()) + System.lineSeparator();
        Files.writeString(path, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<HealthEvent> query(String serviceFilter) throws IOException {
        List<HealthEvent> out = new ArrayList<>();
        if (!Files.exists(path)) {
            return out;
        }
        try (Stream<String> lines = Files.lines(path)) {
            for (String line : lines.toList()) {
                if (line.isBlank()) {
                    continue;
                }
                HealthEvent event = Json.parseEvent(line);
                if (serviceFilter == null || serviceFilter.equals(event.service())) {
                    out.add(event);
                }
            }
        }
        return out;
    }
}
