package flynn.HA3.aufgabe1.code;

import flynn.HA3.aufgabe1.code.generated.LogMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class LogClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        LogMessage message = LogMessage.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setOrigin("flynn")
                .setSeverity(LogMessage.Severity.INFO)
                .setText("das ist eine test-nachricht")
                .build();

        Socket socket = new Socket(HOST, PORT); // wartet, bis die verbindung steht
        OutputStream out = socket.getOutputStream();
        message.writeDelimitedTo(out); // schickt erst die länge, dann die nachricht
        out.flush();

        socket.close();
    }
}
