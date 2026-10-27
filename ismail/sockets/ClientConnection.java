package ismail.sockets;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

final class ClientConnection implements Closeable {
    private final InputStream input;
    private final Closeable resource;

    ClientConnection(InputStream input, Closeable resource) {
        this.input = input;
        this.resource = resource;
    }

    InputStream input() {
        return input;
    }

    @Override
    public void close() throws IOException {
        resource.close();
    }
}
