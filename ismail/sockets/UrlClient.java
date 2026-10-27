package ismail.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class UrlClient extends AbstractClient {
    public UrlClient(String host, String path, int port) {
        super(host, path, port);
    }

    @Override
    protected ClientConnection connect() throws IOException {
        final URLConnection connection = configureTimeouts(
                createUrl(host, path, port).openConnection());
        final InputStream input = connection.getInputStream();
        return connection(input);
    }

    public static void main(String[] args) throws IOException {
        final Arguments arguments = parseArguments(args, "UrlClient");
        if (arguments != null) {
            new UrlClient(arguments.host(), arguments.path(), arguments.port()).run();
        }
    }
}