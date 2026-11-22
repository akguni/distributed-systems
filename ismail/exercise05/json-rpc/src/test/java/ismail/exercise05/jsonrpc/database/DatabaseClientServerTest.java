package ismail.exercise05.jsonrpc.database;

import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseClientServerTest {
    @Test
    void exerciseScenarioStoresSixRecordsAndReadsTheRequestedKeys()
            throws Exception {
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
            final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
            final String[] values = {
                    "Appen", "Ahrensburg", "Wedel",
                    "Aumühle", "Seevetal", "Quickborn"
            };

            for (int i = 0; i < indexes.length; i++) {
                final JsonObject response = DatabaseClient.addRecord(
                        "localhost",
                        server.getLocalPort(),
                        indexes[i],
                        values[i],
                        i + 1);
                assertTrue(response.getBoolean("result"));
            }

            final JsonObject presentResponse = DatabaseClient.getRecord(
                    "localhost", server.getLocalPort(), 4103, 7);
            final JsonObject missingResponse = DatabaseClient.getRecord(
                    "localhost", server.getLocalPort(), 4107, 8);
            final JsonObject sizeResponse = DatabaseClient.getSize(
                    "localhost", server.getLocalPort(), 9);

            assertEquals("Wedel", presentResponse.getString("result"));
            assertTrue(missingResponse.isNull("result"));
            assertEquals(6, sizeResponse.getInt("result"));
        } finally {
            server.close();
            runner.shutdownNow();
            runner.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

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
            final JsonObject addResponse = DatabaseClient.addRecord(
                    "localhost", server.getLocalPort(), 4103, "Wedel", 1);
            final JsonObject presentResponse = DatabaseClient.getRecord(
                    "localhost", server.getLocalPort(), 4103, 2);
            final JsonObject missingResponse = DatabaseClient.getRecord(
                    "localhost", server.getLocalPort(), 4107, 3);
            final JsonObject sizeResponse = DatabaseClient.getSize(
                    "localhost", server.getLocalPort(), 4);

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
}
