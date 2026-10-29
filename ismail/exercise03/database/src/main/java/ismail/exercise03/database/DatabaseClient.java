package ismail.exercise03.database;

import ismail.exercise03.database.protocol.DatabaseRequest;
import ismail.exercise03.database.protocol.DatabaseResponse;

import java.io.IOException;
import java.net.Socket;

public class DatabaseClient {
    private static final int DEFAULT_PORT = 8081;

    private DatabaseClient() {
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Usage: java DatabaseClient.java [host] [port]");
            return;
        }

        final String host = args.length > 0 ? args[0] : "localhost";
        final int port = args.length > 1
                ? Integer.parseInt(args[1])
                : DEFAULT_PORT;
        runExerciseScenario(host, port);
    }

    static void runExerciseScenario(String host, int port) throws IOException {
        final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
        final String[] values = {
                "Appen", "Ahrensburg", "Wedel", "Aumühle", "Seevetal", "Quickborn"
        };

        for (int i = 0; i < indexes.length; i++) {
            final DatabaseResponse response = addRecord(
                    host, port, indexes[i], values[i]);
            printResponse("addRecord(" + indexes[i] + ")", response);
        }

        printResponse("getRecord(4103)", getRecord(host, port, 4103));
        printResponse("getRecord(4107)", getRecord(host, port, 4107));
        printResponse("getSize()", getSize(host, port));
    }

    static DatabaseResponse addRecord(
            String host,
            int port,
            int index,
            String record) throws IOException {
        return send(host, port, DatabaseRequest.newBuilder()
                .setOperation(DatabaseRequest.Operation.ADD_RECORD)
                .setIndex(index)
                .setRecord(record)
                .build());
    }

    static DatabaseResponse getRecord(String host, int port, int index)
            throws IOException {
        return send(host, port, DatabaseRequest.newBuilder()
                .setOperation(DatabaseRequest.Operation.GET_RECORD)
                .setIndex(index)
                .build());
    }

    static DatabaseResponse getSize(String host, int port) throws IOException {
        return send(host, port, DatabaseRequest.newBuilder()
                .setOperation(DatabaseRequest.Operation.GET_SIZE)
                .build());
    }

    private static DatabaseResponse send(
            String host,
            int port,
            DatabaseRequest request) throws IOException {
        try (Socket socket = new Socket(host, port)) {
            request.writeDelimitedTo(socket.getOutputStream());
            socket.shutdownOutput();
            final DatabaseResponse response =
                    DatabaseResponse.parseDelimitedFrom(socket.getInputStream());
            if (response == null) {
                throw new IOException("Database server closed without a response");
            }
            return response;
        }
    }

    private static void printResponse(String operation, DatabaseResponse response) {
        if (response.getSuccess()) {
            if (response.getRecord().isEmpty()) {
                System.out.println(operation + " succeeded"
                        + (response.getSize() > 0
                        ? ": " + response.getSize() : ""));
            } else {
                System.out.println(operation + " -> " + response.getRecord());
            }
        } else {
            System.out.println(operation + " failed: " + response.getError());
        }
    }
}
