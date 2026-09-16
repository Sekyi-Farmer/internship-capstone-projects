package com.genkey.healthlogger;

import com.genkey.healthlogger.model.HealthEvent;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Tiny local HTTP surface (JDK {@link HttpServer}, not a servlet container).
 * POST /events — record; GET /health — liveness.
 */
public final class HealthHttpServer {

    private final ClientSpec spec;
    private final JsonlStore store;
    private HttpServer server;

    public HealthHttpServer(ClientSpec spec, JsonlStore store) {
        this.spec = spec;
        this.store = store;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", spec.port()), 0);
        server.createContext("/health", this::health);
        server.createContext("/events", this::events);
        server.start();
        System.err.println("listening on http://127.0.0.1:" + spec.port());
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void health(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) {
            send(ex, 405, "{\"error\":\"method not allowed\"}");
            return;
        }
        send(ex, 200, "{\"status\":\"ok\"}");
    }

    private void events(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) {
            send(ex, 405, "{\"error\":\"method not allowed\"}");
            return;
        }
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        HealthEvent event = Json.parseEvent(body);
        List<String> issues = Json.validate(event, spec);
        if (!issues.isEmpty()) {
            send(ex, 400, Json.toJson(Map.of("issues", issues)));
            return;
        }
        store.append(event);
        StructuredLogger.event(spec, event);
        send(ex, 201, Json.toJson(event.fields()));
    }

    private static void send(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
