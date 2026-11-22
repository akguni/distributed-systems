package ismail.exercise05.jsonrpc.database;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseServer {
    private static final int DEFAULT_PORT = 8081;
    private static final int WORKER_COUNT = Math.max(
            2, Runtime.getRuntime().availableProcessors());

    private DatabaseServer() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: java DatabaseServer.java [port]");
            return;
        }

        final int port = args.length == 1
                ? Integer.parseInt(args[0])
                : DEFAULT_PORT;
        try (ServerSocket server = new ServerSocket(port)) {
            run(server, new InMemoryDatabase());
        }
    }

    static ServerSocket start(int port) throws IOException {
        return new ServerSocket(port);
    }

    static void run(ServerSocket server, InMemoryDatabase database)
            throws IOException {
        final ExecutorService workers = Executors.newFixedThreadPool(WORKER_COUNT);
        System.out.println("DatabaseServer listening on port "
                + server.getLocalPort());
        try {
            while (!server.isClosed()) {
                try {
                    final Socket client = server.accept();
                    workers.execute(() -> handle(client, database));
                } catch (SocketException exception) {
                    if (server.isClosed()) {
                        break;
                    }
                    throw exception;
                }
            }
        } finally {
            workers.shutdown();
            try {
                if (!workers.awaitTermination(5, TimeUnit.SECONDS)) {
                    workers.shutdownNow();
                }
            } catch (InterruptedException exception) {
                workers.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void handle(Socket client, InMemoryDatabase database) {
        try (client;
             BufferedReader input = new BufferedReader(new InputStreamReader(
                     client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter output = new PrintWriter(
                     client.getOutputStream(), false, StandardCharsets.UTF_8)) {
            String line;
            while ((line = input.readLine()) != null) {
                final JsonObject response = dispatchJson(line, database);
                if (!response.isEmpty()) {
                    output.println(response);
                    output.flush();
                }
            }
        } catch (IOException exception) {
            System.err.println("Database client connection failed: "
                    + exception.getMessage());
        }
    }

    static JsonObject dispatchJson(String message, InMemoryDatabase database) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            final JsonObject request = reader.readObject();
            if (!"2.0".equals(request.getString("jsonrpc", ""))) {
                return errorResponse(request, -32600, "Invalid Request");
            }

            final String method = request.getString("method", "");
            final JsonValue id = request.containsKey("id")
                    ? request.get("id")
                    : null;
            if (id == null) {
                dispatchMethod(request, database);
                return JsonObject.EMPTY_JSON_OBJECT;
            }

            final JsonObject result = switch (method) {
                case "getRecord" -> getRecord(request, database);
                case "addRecord" -> addRecord(request, database);
                case "getSize" -> getSize(request, database);
                default -> errorResponse(request, -32601, "Method not found");
            };
            return result;
        } catch (JsonException | NullPointerException | IllegalStateException exception) {
            return Json.createObjectBuilder()
                    .add("jsonrpc", "2.0")
                    .add("error", Json.createObjectBuilder()
                            .add("code", -32700)
                            .add("message", "Parse error"))
                    .add("id", JsonValue.NULL)
                    .build();
        }
    }

    private static void dispatchMethod(
            JsonObject request,
            InMemoryDatabase database) {
        switch (request.getString("method", "")) {
            case "getRecord" -> getRecord(request, database);
            case "addRecord" -> addRecord(request, database);
            case "getSize" -> getSize(request, database);
            default -> {
                // JSON-RPC notifications do not receive a response.
            }
        }
    }

    private static JsonObject getRecord(
            JsonObject request,
            InMemoryDatabase database) {
        try {
            final JsonObject params = request.getJsonObject("params");
            final int key = params.getInt("key");
            final JsonValue id = request.get("id");
            return database.get(key)
                    .map(record -> Json.createObjectBuilder()
                            .add("jsonrpc", "2.0")
                            .add("result", record)
                            .add("id", id)
                            .build())
                    .orElseGet(() -> Json.createObjectBuilder()
                            .add("jsonrpc", "2.0")
                            .add("result", JsonValue.NULL)
                            .add("id", id)
                            .build());
        } catch (NullPointerException | ClassCastException
                 | IllegalStateException | ArithmeticException exception) {
            return errorResponse(request, -32602, "Invalid params");
        }
    }

    private static JsonObject addRecord(
            JsonObject request,
            InMemoryDatabase database) {
        try {
            final JsonObject params = request.getJsonObject("params");
            final int key = params.getInt("key");
            final String record = params.getString("record");
            final JsonValue id = request.get("id");
            if (!database.add(key, record)) {
                return Json.createObjectBuilder()
                        .add("jsonrpc", "2.0")
                        .add("error", Json.createObjectBuilder()
                                .add("code", -32000)
                                .add("message", "Record already exists at key "
                                        + key))
                        .add("id", id)
                        .build();
            }
            return Json.createObjectBuilder()
                    .add("jsonrpc", "2.0")
                    .add("result", true)
                    .add("id", id)
                    .build();
        } catch (NullPointerException | ClassCastException
                 | IllegalStateException | ArithmeticException exception) {
            return errorResponse(request, -32602, "Invalid params");
        }
    }

    private static JsonObject getSize(
            JsonObject request,
            InMemoryDatabase database) {
        final JsonValue id = request.get("id");
        return Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("result", database.size())
                .add("id", id)
                .build();
    }

    private static JsonObject errorResponse(
            JsonObject request,
            int code,
            String message) {
        final JsonValue id = request.containsKey("id")
                ? request.get("id")
                : JsonValue.NULL;
        return Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("error", Json.createObjectBuilder()
                        .add("code", code)
                        .add("message", message))
                .add("id", id)
                .build();
    }
}
