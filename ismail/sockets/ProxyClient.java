package ismail.sockets;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLConnection;

public class ProxyClient extends AbstractClient {
    private static final String PROXY_HOST = "proxy.fh-wedel.de";
    private static final int PROXY_PORT = 3128;

    public ProxyClient(String host, String path, int port) {
        super(host, path, port);
    }

    @Override
    protected ClientConnection connect() throws IOException {
        final Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(PROXY_HOST, PROXY_PORT));

        final URLConnection connection = configureTimeouts(
                createUrl(host, path, port).openConnection(proxy));
        final InputStream input = connection.getInputStream();
        return connection(input);
    }

    public static void main(String[] args) throws IOException {
        final Arguments arguments = parseArguments(args, "ProxyClient");
        if (arguments != null) {
            new ProxyClient(arguments.host(), arguments.path(), arguments.port()).run();
        }
    }
}