package ismail.exercise03.logging;

import logging.LogMessageOuterClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogClientServerTest {
    @Test
    void clientMessageIsReceivedAndStored(@TempDir Path temporaryDirectory)
            throws Exception {
        final Path logFile = temporaryDirectory.resolve("log.txt");
        final ServerSocket server = new ServerSocket(0);
        final AtomicReference<Throwable> serverFailure = new AtomicReference<>();
        final Thread serverThread = new Thread(() -> {
            try {
                LogServer.run(server, logFile);
            } catch (IOException exception) {
                serverFailure.set(exception);
            }
        });

        serverThread.start();
        LogClient.send(
                "localhost",
                server.getLocalPort(),
                LogMessageOuterClass.Severity.INFO,
                "integration test message",
                "integration-test",
                "logging");

        waitForLogEntry(logFile);
        server.close();
        serverThread.join(5_000);

        assertFalse(serverThread.isAlive());
        assertTrue(serverFailure.get() == null, () -> serverFailure.get().toString());
        assertTrue(
                Files.readString(logFile).contains(
                        "[INFO] integration-test@logging: integration test message"));
    }

    private static void waitForLogEntry(Path logFile) throws Exception {
        for (int attempt = 0; attempt < 50; attempt++) {
            if (Files.exists(logFile)
                    && Files.readString(logFile).contains("integration test message")) {
                return;
            }
            Thread.sleep(10);
        }
    }
}
