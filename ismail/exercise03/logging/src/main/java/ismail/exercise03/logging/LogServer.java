package ismail.exercise03.logging;

import com.google.protobuf.InvalidProtocolBufferException;
import logging.LogMessageOuterClass;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public class LogServer {
    private static final int DEFAULT_PORT = 8080;
    private static final Path DEFAULT_LOG_FILE = Path.of("log_entries.log");

    private LogServer() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.printf("Usage: java LogServer.java [port] [log-file]%n");
            return;
        }

        final int port = args.length > 0
                ? Integer.parseInt(args[0])
                : DEFAULT_PORT;
        final Path logFile = args.length > 1
                ? Path.of(args[1])
                : DEFAULT_LOG_FILE;

        try (ServerSocket server = new ServerSocket(port)) {
            run(server, logFile);
        }
    }

    static void run(ServerSocket server, Path logFile) throws IOException {
        try (BufferedWriter log = Files.newBufferedWriter(
                logFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            System.out.println("LogServer listening on port " + server.getLocalPort()
                    + ", writing to " + logFile.toAbsolutePath());
            while (!server.isClosed()) {
                try (Socket client = server.accept()) {
                    receiveMessages(client.getInputStream(), log);
                } catch (SocketException exception) {
                    if (server.isClosed()) {
                        break;
                    }
                    throw exception;
                } catch (InvalidProtocolBufferException exception) {
                    System.err.println("Ignoring malformed log message: "
                            + exception.getMessage());
                }
            }
        }
    }

    private static void receiveMessages(InputStream input, BufferedWriter log)
            throws IOException {
        LogMessageOuterClass.LogMessage message;
        while ((message = LogMessageOuterClass.LogMessage.parseDelimitedFrom(input))
                != null) {
            final String entry = format(message);
            log.write(entry);
            log.newLine();
            log.flush();
            System.out.println("Received: " + entry);
        }
    }

    private static String format(LogMessageOuterClass.LogMessage message) {
        final String timestamp = message.hasTimestamp()
                ? Instant.ofEpochSecond(
                        message.getTimestamp().getSeconds(),
                        message.getTimestamp().getNanos()).toString()
                : "unknown-time";
        final String createdBy = message.hasIndicator()
                ? message.getIndicator().getCreatedBy()
                : "unknown-creator";
        final String location = message.hasIndicator()
                ? message.getIndicator().getLocation()
                : "unknown-location";
        return timestamp + " [" + message.getSeverity() + "] "
                + oneLine(createdBy) + "@" + oneLine(location) + ": "
                + oneLine(message.getText());
    }

    private static String oneLine(String value) {
        return value.replace('\r', ' ').replace('\n', ' ');
    }
}
