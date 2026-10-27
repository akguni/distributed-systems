package ismail.sockets;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SimpleClient extends AbstractClient {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private static final String GET_METHOD = "GET";
    private static final String HOST_HEADER = "Host: ";
    private static final String CONNECTION_CLOSE_HEADER = "Connection: close";
    private static final String CRLF = "\r\n";

    public SimpleClient(String host, String path, int port) {
        super(host, path, port);
    }

    @Override
    protected ClientConnection connect() throws IOException {
        final Socket socket = new Socket(host, port);
        try {
            final OutputStream out = socket.getOutputStream();
            final String request =
                    GET_METHOD + " " + path + " " + HTTP_VERSION + CRLF +
                            HOST_HEADER + host + CRLF +
                            CONNECTION_CLOSE_HEADER + CRLF +
                            CRLF;

            out.write(request.getBytes(StandardCharsets.US_ASCII));
            out.flush();
            return new ClientConnection(socket.getInputStream(), socket);
        } catch (IOException exception) {
            socket.close();
            throw exception;
        }
    }

    public static void main(String[] args) throws IOException {
        final Arguments arguments = parseArguments(args, "SimpleClient");
        if (arguments != null) {
            new SimpleClient(arguments.host(), arguments.path(), arguments.port()).run();
        }
    }
}