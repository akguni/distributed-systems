package ismail.exercise05.jsonrpc.database;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class DatabaseClient {
    private static final int DEFAULT_PORT = 8081;

    private DatabaseClient() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Usage: java DatabaseClient.java [host] [port]");
            return;
        }

        final String host = args.length > 0 ? args[0] : "localhost";
        final int port = args.length > 1
                ? Integer.parseInt(args[1])
                : DEFAULT_PORT;
        runExerciseScenario(host, port);
    }

    static void runExerciseScenario(String host, int port) throws IOException {
        final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
        final String[] values = {
                "Appen", "Ahrensburg", "Wedel", "Aumühle", "Seevetal", "Quickborn"
        };

        for (int i = 0; i < indexes.length; i++) {
            final JsonObject response =
                    addRecord(host, port, indexes[i], values[i], i + 1);
            printAddResponse(indexes[i], response);
        }

        printGetResponse(host, port, 4103, indexes.length + 1);
        printGetResponse(host, port, 4107, indexes.length + 2);
        final JsonObject sizeResponse =
                getSize(host, port, indexes.length + 3);
        if (sizeResponse.containsKey("error")) {
            printError("getSize()", sizeResponse);
        } else {
            System.out.println("getSize() -> "
                    + sizeResponse.getInt("result"));
        }
    }

    static JsonObject addRecord(
            String host,
            int port,
            int key,
            String record,
            int id) throws IOException {
        final JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "addRecord")
                .add("params", Json.createObjectBuilder()
                        .add("key", key)
                        .add("record", record))
                .add("id", id)
                .build();
        return send(host, port, request);
    }

    static JsonObject getRecord(
            String host,
            int port,
            int key,
            int id) throws IOException {
        final JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "getRecord")
                .add("params", Json.createObjectBuilder()
                        .add("key", key))
                .add("id", id)
                .build();
        return send(host, port, request);
    }

    static JsonObject getSize(
            String host,
            int port,
            int id) throws IOException {
        final JsonObject request = Json.createObjectBuilder()
                .add("jsonrpc", "2.0")
                .add("method", "getSize")
                .add("params", Json.createArrayBuilder())
                .add("id", id)
                .build();
        return send(host, port, request);
    }

    private static JsonObject send(
            String host,
            int port,
            JsonObject request) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader input = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8))) {
            output.println(request);
            final String line = input.readLine();
            if (line == null) {
                throw new IOException("Database server closed without a response");
            }
            try (JsonReader reader = Json.createReader(new StringReader(line))) {
                return reader.readObject();
            }
        }
    }

    private static void printAddResponse(int key, JsonObject response) {
        if (response.containsKey("error")) {
            printError("addRecord(" + key + ")", response);
        } else {
            System.out.println(response.getBoolean("result")
                    ? "addRecord(" + key + ") succeeded"
                    : "addRecord(" + key + ") failed");
        }
    }

    private static void printGetResponse(
            String host,
            int port,
            int key,
            int id) throws IOException {
        final JsonObject response = getRecord(host, port, key, id);
        if (response.containsKey("error")) {
            printError("getRecord(" + key + ")", response);
        } else if (response.isNull("result")) {
            System.out.println("getRecord(" + key
                    + ") reports that no record exists");
        } else {
            System.out.println("getRecord(" + key + ") -> "
                    + response.getString("result"));
        }
    }

    private static void printError(String operation, JsonObject response) {
        final JsonObject error = response.getJsonObject("error");
        System.out.println(operation + " failed: "
                + error.getString("message"));
    }
}
