GRPC DATABASE PART OF EXERCISE 04
==================================

This module implements the database server and client using Java gRPC and
Protocol Buffers.

REQUIREMENTS

Java 21 or newer
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

JSON-RPC 2.0 REQUEST AND RESPONSE FORMS
========================================

The Exercise 05 JSON-RPC alternative uses JSON-RPC 2.0 messages. Each request
contains a method name, parameters, and a request id. Responses contain the
same id and either a result or an error.

getRecord
---------

Request:

{"jsonrpc":"2.0","method":"getRecord","params":{"key":4103},"id":1}

Response when the record exists:

{"jsonrpc":"2.0","result":"Wedel","id":1}

Request for a missing key:

{"jsonrpc":"2.0","method":"getRecord","params":{"key":4107},"id":2}

Response:

{"jsonrpc":"2.0","result":null,"id":2}

addRecord
---------

Request:

{"jsonrpc":"2.0","method":"addRecord","params":{"key":4101,"record":"Appen"},"id":3}

Response:

{"jsonrpc":"2.0","result":true,"id":3}

getSize
-------

Request:

{"jsonrpc":"2.0","method":"getSize","params":[],"id":4}

Response:

{"jsonrpc":"2.0","result":6,"id":4}

Invalid parameters or another JSON-RPC failure can be returned as an error:

{"jsonrpc":"2.0","error":{"code":-32602,"message":"Invalid params"},"id":2}
