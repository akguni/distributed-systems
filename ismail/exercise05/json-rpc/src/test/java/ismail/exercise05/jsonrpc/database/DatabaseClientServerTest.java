package ismail.exercise05.jsonrpc.database;

import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseClientServerTest {
    @Test
    void clientAndServerExchangeAllOperations() throws Exception {
        final ServerSocket server = DatabaseServer.start(0);
        final ExecutorService runner = Executors.newSingleThreadExecutor();
        runner.execute(() -> {
            try {
                DatabaseServer.run(server, new InMemoryDatabase());
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        });
        try {
            final JsonObject addResponse = send(
                    server.getLocalPort(),
                    "{\"jsonrpc\":\"2.0\",\"method\":\"addRecord\","
                            + "\"params\":{\"key\":4103,\"record\":\"Wedel\"},"
                            + "\"id\":1}");
            final JsonObject presentResponse = send(
                    server.getLocalPort(),
                    "{\"jsonrpc\":\"2.0\",\"method\":\"getRecord\","
                            + "\"params\":{\"key\":4103},\"id\":2}");
            final JsonObject missingResponse = send(
                    server.getLocalPort(),
                    "{\"jsonrpc\":\"2.0\",\"method\":\"getRecord\","
                            + "\"params\":{\"key\":4107},\"id\":3}");
            final JsonObject sizeResponse = send(
                    server.getLocalPort(),
                    "{\"jsonrpc\":\"2.0\",\"method\":\"getSize\","
                            + "\"params\":[],\"id\":4}");

            assertTrue(addResponse.getBoolean("result"));
            assertEquals("Wedel", presentResponse.getString("result"));
            assertTrue(missingResponse.isNull("result"));
            assertEquals(1, sizeResponse.getInt("result"));
        } finally {
            server.close();
            runner.shutdownNow();
            runner.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    private static JsonObject send(int port, String request) throws Exception {
        try (Socket socket = new Socket("localhost", port);
             PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader input = new BufferedReader(new InputStreamReader(
                     socket.getInputStream(), StandardCharsets.UTF_8))) {
            output.println(request);
            final String response = input.readLine();
            try (JsonReader reader =
                         Json.createReader(new StringReader(response))) {
                return reader.readObject();
            }
        }
    }
}
