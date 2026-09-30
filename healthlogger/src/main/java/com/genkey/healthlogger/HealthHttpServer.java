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
 * Tiny local HTTP surface (JDK HttpServer, not a servlet container).
 *
 * POST /events — record a valid event or save a rejected submission.
 * GET /health  — liveness check.
 */
public final class HealthHttpServer {

    private final ClientSpec spec;
    private final JsonlStore store;
    private final RejectedEventStore rejectedStore;
    private HttpServer server;

    public HealthHttpServer(
            ClientSpec spec,
            JsonlStore store,
            RejectedEventStore rejectedStore
    ) {
        this.spec = spec;
        this.store = store;
        this.rejectedStore = rejectedStore;
    }

    public void start() throws IOException {
        server = HttpServer.create(
                new InetSocketAddress("127.0.0.1", spec.port()),
                0
        );

        server.createContext("/health", this::health);
        server.createContext("/events", this::events);

        server.start();

        System.err.println(
                "listening on http://127.0.0.1:" + spec.port()
        );
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void health(HttpExchange ex) throws IOException {
        if (!"GET".equals(ex.getRequestMethod())) {
            send(
                    ex,
                    405,
                    "{\"error\":\"method not allowed\"}"
            );
            return;
        }

        send(
                ex,
                200,
                "{\"status\":\"ok\"}"
        );
    }

    private void events(HttpExchange ex) throws IOException {
        if (!"POST".equals(ex.getRequestMethod())) {
            send(
                    ex,
                    405,
                    "{\"error\":\"method not allowed\"}"
            );
            return;
        }

        String body = new String(
                ex.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
        );

        HealthEvent event;

        /*
         * STEP 1:
         * Try to parse the incoming JSON.
         */
        try {
            event = Json.parseEvent(body);
        } catch (Exception e) {

            /*
             * Malformed JSON:
             * save the original request body as a rejection.
             */
            rejectedStore.append(
                    body,
                    "malformed JSON: " + e.getMessage()
            );

            send(
                    ex,
                    400,
                    Json.toJson(
                            Map.of(
                                    "error",
                                    "malformed JSON"
                            )
                    )
            );
            return;
        }

        /*
         * STEP 2:
         * JSON was valid, so validate the event.
         */
        List<String> issues = Json.validate(event, spec);

        if (!issues.isEmpty()) {

            /*
             * Valid JSON but invalid event:
             * save it separately from valid events.
             */
            rejectedStore.append(
                    event.fields(),
                    String.join("; ", issues)
            );

            send(
                    ex,
                    400,
                    Json.toJson(
                            Map.of(
                                    "issues",
                                    issues
                            )
                    )
            );
            return;
        }

        /*
         * STEP 3:
         * Everything is valid.
         * Save only to the valid event store.
         */
        store.append(event);

        StructuredLogger.event(spec, event);

        send(
                ex,
                201,
                Json.toJson(event.fields())
        );
    }

    private static void send(
            HttpExchange ex,
            int code,
            String json
    ) throws IOException {

        byte[] bytes = json.getBytes(
                StandardCharsets.UTF_8
        );

        ex.getResponseHeaders().set(
                "Content-Type",
                "application/json"
        );

        ex.sendResponseHeaders(
                code,
                bytes.length
        );

        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}