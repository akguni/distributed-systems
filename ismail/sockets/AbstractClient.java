package ismail.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public abstract class AbstractClient {
    protected static final int DEFAULT_HTTP_PORT = 80;
    protected static final String HTTP_SCHEME = "http";
    protected static final int CONNECTION_TIMEOUT_MILLISECONDS = 5_000;
    protected static final int READ_TIMEOUT_MILLISECONDS = 5_000;
    private static final int MIN_ARGUMENTS = 2;
    private static final int MAX_ARGUMENTS = 3;
    private static final String USAGE_FORMAT = "Usage: java %s.java <host> <path> [port]%n";
    private static final String INVALID_URL_MESSAGE = "Invalid request URL";

    protected final String host;
    protected final String path;
    protected final int port;

    protected AbstractClient(String host, String path, int port) {
        this.host = host;
        this.path = path;
        this.port = port;
    }

    protected abstract ClientConnection connect() throws IOException;

    protected static Arguments parseArguments(String[] args, String clientName) {
        if (args.length < MIN_ARGUMENTS || args.length > MAX_ARGUMENTS) {
            System.out.printf(USAGE_FORMAT, clientName);
            return null;
        }

        final String host = args[0];
        final String path = args[1];
        final int port = args.length == MAX_ARGUMENTS
                ? Integer.parseInt(args[2])
                : DEFAULT_HTTP_PORT;
        return new Arguments(host, path, port);
    }

    protected static URL createUrl(String host, String path, int port) throws IOException {
        try {
            return new URI(HTTP_SCHEME, null, host, port, path, null, null).toURL();
        } catch (URISyntaxException exception) {
            throw new IOException(INVALID_URL_MESSAGE, exception);
        }
    }

    protected ClientConnection connection(InputStream input) {
        return new ClientConnection(input, input);
    }

    protected URLConnection configureTimeouts(URLConnection connection) {
        connection.setConnectTimeout(CONNECTION_TIMEOUT_MILLISECONDS);
        connection.setReadTimeout(READ_TIMEOUT_MILLISECONDS);
        return connection;
    }

    public final void run() throws IOException {
        try (ClientConnection connection = connect();
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     connection.input(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    protected static final class Arguments {
        private final String host;
        private final String path;
        private final int port;

        private Arguments(String host, String path, int port) {
            this.host = host;
            this.path = path;
            this.port = port;
        }

        protected String host() {
            return host;
        }

        protected String path() {
            return path;
        }

        protected int port() {
            return port;
        }
    }
}
