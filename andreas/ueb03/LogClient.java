package andreas.ueb03;

import andreas.ueb03.proto.LogMessage;
import andreas.ueb03.proto.Origin;
import andreas.ueb03.proto.Severity;
import com.google.protobuf.Timestamp;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Instant;

// Blatt 3, Teil A, Aufgabe 4: Client schickt ein paar Log-Nachrichten an den LogServer
public class LogClient {

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 9090;

        Origin origin = Origin.newBuilder()
                .setHost(InetAddress.getLocalHost().getHostName())
                .setProgram("LogClient")
                .build();

        try (Socket socket = new Socket(host, port)) {
            OutputStream out = socket.getOutputStream();

            send(out, Severity.INFO, "Client started", origin);
            send(out, Severity.DEBUG, "Connected to " + host + ":" + port, origin);
            send(out, Severity.WARN, "Disk almost full", origin);
            send(out, Severity.ERROR, "Could not open config file", origin);
            send(out, Severity.FATAL, "Shutting down", null);   // absichtlich ohne origin, Feld ist optional
            out.flush();
        }
        System.out.println("5 log messages sent");
    }

    static void send(OutputStream out, Severity severity, String text, Origin origin) throws Exception {
        Instant now = Instant.now();
        LogMessage.Builder builder = LogMessage.newBuilder()
                .setTimestamp(Timestamp.newBuilder().setSeconds(now.getEpochSecond()).setNanos(now.getNano()))
                .setSeverity(severity)
                .setText(text);
        if (origin != null) {
            builder.setOrigin(origin);
        }
        // mit Längenpräfix schicken, sonst weiß der Server nicht wo eine Nachricht endet (TCP ist nur ein Stream)
        builder.build().writeDelimitedTo(out);
    }
}
