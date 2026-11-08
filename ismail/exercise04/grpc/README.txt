GRPC DATABASE PART OF EXERCISE 04
==================================

This module implements the database server and client using Java gRPC and
Protocol Buffers.

REQUIREMENTS

Java 25 or newer
Maven

BUILD

From the exercise04 directory, run:

mvn clean test

The protobuf messages and gRPC stubs are generated automatically from
grpc/src/main/proto/Database.proto during the Maven build.

PREPARE THE CLASSPATH

From the grpc directory, run:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

START THE SERVER

In one terminal:

java -cp "$CP" ismail.exercise04.database.DatabaseServer 8081

The optional argument is the gRPC port. The default is 8081.
The server prints a listening message after startup and reports each RPC it
processes.

RUN THE CLIENT SCENARIO

In another terminal, prepare CP again if necessary:

CP="target/classes:$(cat target/classpath.txt)"

Then run:

java -cp "$CP" ismail.exercise04.database.DatabaseClient localhost 8081

The client adds these records:

4101  Appen
4102  Ahrensburg
4103  Wedel
4104  Aumühle
4105  Seevetal
4106  Quickborn

It then reads records 4103 and 4107 and requests the database size.
The expected results are:

getRecord(4103) -> Wedel
getRecord(4107) reports that no record exists
getSize() -> 6

The database is stored in memory and is lost when the server stops.

TESTS

The module contains unit tests for the in-memory database and an integration
test that starts a gRPC server and exercises the generated blocking client
stub.

Run the database tests from this directory with:

mvn test
