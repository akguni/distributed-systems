package ismail.exercise03.database;

import com.google.protobuf.InvalidProtocolBufferException;
import ismail.exercise03.database.protocol.DatabaseRequest;
import ismail.exercise03.database.protocol.DatabaseResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DatabaseServer {
    private static final int DEFAULT_PORT = 8081;
    private static final int WORKER_COUNT = Math.max(
            2, Runtime.getRuntime().availableProcessors());

    private DatabaseServer() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: java DatabaseServer.java [port]");
            return;
        }

        final int port = args.length == 1
                ? Integer.parseInt(args[0])
                : DEFAULT_PORT;
        try (ServerSocket server = new ServerSocket(port)) {
            run(server, new InMemoryDatabase());
        }
    }

    static void run(ServerSocket server, InMemoryDatabase database)
            throws IOException {
        final ExecutorService workers = Executors.newFixedThreadPool(WORKER_COUNT);
        System.out.println("DatabaseServer listening on port "
                + server.getLocalPort());
        try {
            while (!server.isClosed()) {
                try {
                    final Socket client = server.accept();
                    workers.execute(() -> handle(client, database));
                } catch (SocketException exception) {
                    if (server.isClosed()) {
                        break;
                    }
                    throw exception;
                }
            }
        } finally {
            workers.shutdown();
            try {
                if (!workers.awaitTermination(5, TimeUnit.SECONDS)) {
                    workers.shutdownNow();
                }
            } catch (InterruptedException exception) {
                workers.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void handle(Socket client, InMemoryDatabase database) {
        try (client;
             InputStream input = client.getInputStream();
             OutputStream output = client.getOutputStream()) {
            final DatabaseRequest request =
                    DatabaseRequest.parseDelimitedFrom(input);
            if (request == null) {
                return;
            }

            final DatabaseResponse response = dispatch(request, database);
            response.writeDelimitedTo(output);
            output.flush();
            System.out.println("Processed " + request.getOperation());
        } catch (InvalidProtocolBufferException exception) {
            System.err.println("Ignoring malformed database request: "
                    + exception.getMessage());
        } catch (IOException exception) {
            System.err.println("Database client connection failed: "
                    + exception.getMessage());
        }
    }

    private static DatabaseResponse dispatch(
            DatabaseRequest request,
            InMemoryDatabase database) {
        return switch (request.getOperation()) {
            case ADD_RECORD -> addRecord(request, database);
            case GET_RECORD -> getRecord(request, database);
            case GET_SIZE -> DatabaseResponse.newBuilder()
                    .setSuccess(true)
                    .setSize(database.size())
                    .build();
            case OPERATION_UNSPECIFIED, UNRECOGNIZED ->
                    failure("Operation is required");
        };
    }

    private static DatabaseResponse addRecord(
            DatabaseRequest request,
            InMemoryDatabase database) {
        if (!database.add(request.getIndex(), request.getRecord())) {
            return failure("Record already exists at index " + request.getIndex());
        }
        return DatabaseResponse.newBuilder()
                .setSuccess(true)
                .build();
    }

    private static DatabaseResponse getRecord(
            DatabaseRequest request,
            InMemoryDatabase database) {
        return database.get(request.getIndex())
                .map(record -> DatabaseResponse.newBuilder()
                        .setSuccess(true)
                        .setRecord(record)
                        .build())
                .orElseGet(() -> failure(
                        "No record exists at index " + request.getIndex()));
    }

    private static DatabaseResponse failure(String error) {
        return DatabaseResponse.newBuilder()
                .setSuccess(false)
                .setError(error)
                .build();
    }
}
