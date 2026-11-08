package ismail.exercise04.database;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ismail.exercise04.database.protocol.AddRecordRequest;
import ismail.exercise04.database.protocol.AddRecordResponse;
import ismail.exercise04.database.protocol.DatabaseServiceGrpc;
import ismail.exercise04.database.protocol.GetRecordRequest;
import ismail.exercise04.database.protocol.GetRecordResponse;
import ismail.exercise04.database.protocol.GetSizeRequest;
import ismail.exercise04.database.protocol.GetSizeResponse;

import java.io.IOException;
import java.util.Optional;

public class DatabaseServer {
    private static final int DEFAULT_PORT = 8081;

    private DatabaseServer() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length > 1) {
            System.out.println("Usage: java DatabaseServer.java [port]");
            return;
        }

        final int port = args.length == 1
                ? Integer.parseInt(args[0])
                : DEFAULT_PORT;
        final Server server = start(port, new InMemoryDatabase());
        try {
            server.awaitTermination();
        } finally {
            server.shutdown();
        }
    }

    static Server start(int port, InMemoryDatabase database) throws IOException {
        final Server server = ServerBuilder.forPort(port)
                .addService(new DatabaseService(database))
                .build()
                .start();
        System.out.println("DatabaseServer listening on port " + server.getPort());
        return server;
    }

    private static final class DatabaseService
            extends DatabaseServiceGrpc.DatabaseServiceImplBase {
        private final InMemoryDatabase database;

        private DatabaseService(InMemoryDatabase database) {
            this.database = database;
        }

        @Override
        public void addRecord(
                AddRecordRequest request,
                StreamObserver<AddRecordResponse> responseObserver) {
            final boolean added = database.add(request.getIndex(), request.getRecord());
            final AddRecordResponse.Builder response = AddRecordResponse.newBuilder()
                    .setAdded(added);
            if (!added) {
                response.setError("Record already exists at index " + request.getIndex());
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            System.out.println("Processed addRecord(" + request.getIndex() + ")");
        }

        @Override
        public void getRecord(
                GetRecordRequest request,
                StreamObserver<GetRecordResponse> responseObserver) {
            final Optional<String> record = database.get(request.getIndex());
            final GetRecordResponse.Builder response = GetRecordResponse.newBuilder()
                    .setFound(record.isPresent());
            record.ifPresent(response::setRecord);
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
            System.out.println("Processed getRecord(" + request.getIndex() + ")");
        }

        @Override
        public void getSize(
                GetSizeRequest request,
                StreamObserver<GetSizeResponse> responseObserver) {
            responseObserver.onNext(GetSizeResponse.newBuilder()
                    .setSize(database.size())
                    .build());
            responseObserver.onCompleted();
            System.out.println("Processed getSize()");
        }
    }
}
