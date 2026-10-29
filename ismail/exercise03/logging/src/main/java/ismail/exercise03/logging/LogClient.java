package ismail.exercise03.logging;

import com.google.protobuf.Timestamp;
import logging.LogMessageOuterClass;

import java.io.IOException;
import java.net.Socket;
import java.time.Instant;
import java.util.Locale;

public class LogClient {
    private static final String DEFAULT_CREATED_BY = "LogClient";
    private static final String DEFAULT_LOCATION = "unknown";

    private LogClient() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 4 || args.length > 6) {
            System.out.printf(
                    "Usage: java LogClient.java <host> <port> <severity> <text>"
                            + " [created-by] [location]%n");
            return;
        }

        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final LogMessageOuterClass.Severity severity = parseSeverity(args[2]);
        final String text = args[3];
        final String createdBy = args.length > 4 ? args[4] : DEFAULT_CREATED_BY;
        final String location = args.length > 5 ? args[5] : DEFAULT_LOCATION;

        send(host, port, severity, text, createdBy, location);
    }

    static void send(
            String host,
            int port,
            LogMessageOuterClass.Severity severity,
            String text,
            String createdBy,
            String location) throws IOException {
        if (text.contains("\n") || text.contains("\r")) {
            throw new IllegalArgumentException("The log text must be one line");
        }

        final Instant now = Instant.now();
        final LogMessageOuterClass.LogMessage.Indicator indicator =
                LogMessageOuterClass.LogMessage.Indicator.newBuilder()
                        .setCreatedBy(createdBy)
                        .setLocation(location)
                        .build();
        final LogMessageOuterClass.LogMessage message =
                LogMessageOuterClass.LogMessage.newBuilder()
                        .setTimestamp(Timestamp.newBuilder()
                                .setSeconds(now.getEpochSecond())
                                .setNanos(now.getNano())
                                .build())
                        .setIndicator(indicator)
                        .setSeverity(severity)
                        .setText(text)
                        .build();

        try (Socket socket = new Socket(host, port)) {
            message.writeDelimitedTo(socket.getOutputStream());
        }
    }

    private static LogMessageOuterClass.Severity parseSeverity(String value) {
        try {
            final LogMessageOuterClass.Severity severity =
                    LogMessageOuterClass.Severity.valueOf(value.toUpperCase(Locale.ROOT));
            if (severity == LogMessageOuterClass.Severity.SEVERITY_UNSPECIFIED) {
                throw new IllegalArgumentException();
            }
            return severity;
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Severity must be DEBUG, INFO, WARN, ERROR, or FATAL", exception);
        }
    }
}
