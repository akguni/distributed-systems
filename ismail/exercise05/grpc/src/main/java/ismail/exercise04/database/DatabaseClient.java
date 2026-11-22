package ismail.exercise04.database;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ismail.exercise04.database.protocol.AddRecordRequest;
import ismail.exercise04.database.protocol.AddRecordResponse;
import ismail.exercise04.database.protocol.DatabaseServiceGrpc;
import ismail.exercise04.database.protocol.GetRecordRequest;
import ismail.exercise04.database.protocol.GetRecordResponse;
import ismail.exercise04.database.protocol.GetSizeRequest;
import ismail.exercise04.database.protocol.GetSizeResponse;

import java.util.concurrent.TimeUnit;

public class DatabaseClient {
    private static final int DEFAULT_PORT = 8081;

    private DatabaseClient() {
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 2) {
            System.out.println("Usage: java DatabaseClient.java [host] [port]");
            return;
        }

        final String host = args.length > 0 ? args[0] : "localhost";
        final int port = args.length > 1
                ? Integer.parseInt(args[1])
                : DEFAULT_PORT;
        final ManagedChannel channel = channel(host, port);
        try {
            runExerciseScenario(DatabaseServiceGrpc.newBlockingStub(channel));
        } finally {
            channel.shutdown();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    static ManagedChannel channel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    static void runExerciseScenario(DatabaseServiceGrpc.DatabaseServiceBlockingStub stub) {
        final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
        final String[] values = {
                "Appen", "Ahrensburg", "Wedel", "Aumühle", "Seevetal", "Quickborn"
        };

        for (int i = 0; i < indexes.length; i++) {
            printAddResponse(indexes[i], addRecord(stub, indexes[i], values[i]));
        }

        printGetResponse(4103, getRecord(stub, 4103));
        printGetResponse(4107, getRecord(stub, 4107));
        System.out.println("getSize() -> " + getSize(stub).getSize());
    }

    static AddRecordResponse addRecord(
            DatabaseServiceGrpc.DatabaseServiceBlockingStub stub,
            int index,
            String record) {
        return stub.addRecord(AddRecordRequest.newBuilder()
                .setIndex(index)
                .setRecord(record)
                .build());
    }

    static GetRecordResponse getRecord(
            DatabaseServiceGrpc.DatabaseServiceBlockingStub stub,
            int index) {
        return stub.getRecord(GetRecordRequest.newBuilder()
                .setIndex(index)
                .build());
    }

    static GetSizeResponse getSize(
            DatabaseServiceGrpc.DatabaseServiceBlockingStub stub) {
        return stub.getSize(GetSizeRequest.getDefaultInstance());
    }

    private static void printAddResponse(int index, AddRecordResponse response) {
        System.out.println(response.getAdded()
                ? "addRecord(" + index + ") succeeded"
                : "addRecord(" + index + ") failed: " + response.getError());
    }

    private static void printGetResponse(int index, GetRecordResponse response) {
        System.out.println(response.getFound()
                ? "getRecord(" + index + ") -> " + response.getRecord()
                : "getRecord(" + index + ") reports that no record exists");
    }
}
