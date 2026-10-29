DATABASE PART OF EXERCISE 03
=============================

This module implements a direct TCP database server and client using
length-delimited Protocol Buffers messages.

REQUIREMENTS

Java 25 or newer
Maven

BUILD

From this directory, run:

mvn clean compile

To build and test both Exercise 03 modules, run from the exercise03 directory:

mvn clean test

The protobuf Java classes are generated automatically from
src/main/proto/Database.proto during the Maven build.

PREPARE THE CLASSPATH

From this directory, run:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

START THE SERVER

In one terminal:

java -cp "$CP" ismail.exercise03.database.DatabaseServer 8081

The optional argument is the TCP port. The default is 8081.

The server stores records in memory and prints a message when it starts
listening and whenever it processes a request.

RUN THE CLIENT SCENARIO

In another terminal, prepare CP again if necessary:

CP="target/classes:$(cat target/classpath.txt)"

Then run:

java -cp "$CP" ismail.exercise03.database.DatabaseClient localhost 8081

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
getSize() reports 6

The server data is in memory only and is lost when the server stops.

TESTS

The module contains unit tests for the in-memory database and an integration
test that starts a real server socket and exercises add, get, missing-record,
and size operations through the client.

Run the database tests with:

mvn test
