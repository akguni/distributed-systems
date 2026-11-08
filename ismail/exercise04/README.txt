EXERCISE 04 - GRPC DATABASE
============================

This exercise modifies the Exercise 03 database server and client to use
gRPC instead of a custom TCP protocol.

REQUIREMENTS

Java 25 or newer
Maven

BUILD AND TEST

From this directory, run:

mvn clean test

Maven automatically generates both Protocol Buffers classes and gRPC client
and server stubs from:

grpc/src/main/proto/Database.proto

PREPARE THE CLASSPATH

From the grpc directory, run:

cd grpc
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

START THE SERVER

In one terminal:

java -cp "$CP" ismail.exercise04.database.DatabaseServer 8081

The optional argument is the gRPC port. The default is 8081.

RUN THE CLIENT

In another terminal:

cd grpc
CP="target/classes:$(cat target/classpath.txt)"
java -cp "$CP" ismail.exercise04.database.DatabaseClient localhost 8081

The client adds the six records from the exercise, reads indexes 4103 and
4107, and requests the database size. The expected results are:

getRecord(4103) -> Wedel
getRecord(4107) reports that no record exists
getSize() -> 6

EXERCISE 03 VERSUS EXERCISE 04

Exercise 03 uses a custom Protocol Buffers protocol over raw TCP sockets:

- The server uses java.net.ServerSocket and java.net.Socket.
- The client writes delimited protobuf messages to socket streams.
- The server calls parseDelimitedFrom and writeDelimitedTo.
- Its Maven POM has the protobuf runtime and protoc generation, but no gRPC
  runtime or gRPC stub generator.

Exercise 04 uses gRPC:

- The server uses io.grpc.Server and implements the generated
  DatabaseServiceGrpc.DatabaseServiceImplBase.
- The client uses ManagedChannel and a generated blocking stub.
- The service methods are declared as RPCs in Database.proto:
  GetRecord, AddRecord, and GetSize.
- Its Maven POM includes grpc-netty-shaded, grpc-protobuf, grpc-stub, and
  protoc-gen-grpc-java.
- Maven generates DatabaseServiceGrpc and the request/response classes.

The Java package names also distinguish the modules:

Exercise 03:
ismail.exercise03.database

Exercise 04:
ismail.exercise04.database

The Exercise 04 module directory is named grpc to avoid IntelliJ module-name
collisions with Exercise 03. The Java package remains database because the
service is still the database application.
