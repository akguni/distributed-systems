package ismail.exercise03.database;

import ismail.exercise03.database.protocol.DatabaseResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseClientServerTest {
    @Test
    void clientAndServerExchangeAllOperations() throws Exception {
        final ServerSocket server = new ServerSocket(0);
        final AtomicReference<Throwable> serverFailure = new AtomicReference<>();
        final Thread serverThread = new Thread(() -> {
            try {
                DatabaseServer.run(server, new InMemoryDatabase());
            } catch (IOException exception) {
                serverFailure.set(exception);
            }
        });
        serverThread.start();

        final int port = server.getLocalPort();
        final DatabaseResponse addResponse =
                DatabaseClient.addRecord("localhost", port, 4103, "Wedel");
        final DatabaseResponse presentResponse =
                DatabaseClient.getRecord("localhost", port, 4103);
        final DatabaseResponse missingResponse =
                DatabaseClient.getRecord("localhost", port, 4107);
        final DatabaseResponse sizeResponse =
                DatabaseClient.getSize("localhost", port);

        server.close();
        serverThread.join(5_000);

        assertTrue(addResponse.getSuccess());
        assertTrue(presentResponse.getSuccess());
        assertEquals("Wedel", presentResponse.getRecord());
        assertFalse(missingResponse.getSuccess());
        assertTrue(missingResponse.getError().contains("4107"));
        assertTrue(sizeResponse.getSuccess());
        assertEquals(1, sizeResponse.getSize());
        assertFalse(serverThread.isAlive());
        assertTrue(serverFailure.get() == null, () -> serverFailure.get().toString());
    }
}
