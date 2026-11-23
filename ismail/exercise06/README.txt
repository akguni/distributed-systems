EXERCISE 06 - JAVA RMI DATABASE
================================

This project implements a simple Java RMI database server and client.

REQUIREMENTS

Java 21 or newer
Maven

BUILD

From the exercise06 directory, run:

mvn clean test

The project has one Maven module:

java-rmi

RUN THE SERVER MANUALLY

From the exercise06/java-rmi directory, first build the classpath:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

Then start the server:

java -cp "$CP" ismail.exercise06.javarmi.database.DataBaseImpl /tmp/database-stub.bin

The first argument is the file where the exported RMI stub is written.
It is optional. When omitted, the server writes the stub to:

database-stub.bin

Keep the server process running. It writes this startup message:

Database server is running.
Stub written to /tmp/database-stub.bin

INTELLIJ IDEA

Open or reload the exercise06 Maven project, then run
DataBaseImpl with an optional program argument:

/tmp/database-stub.bin

Start it in Debug mode if you want to inspect the server's in-memory database
while running the client.

RUN THE CLIENT MANUALLY

While the server is running, open a second terminal in
exercise06/java-rmi and prepare the classpath:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

Run the client with the same stub-file path used by the server:

java -cp "$CP" ismail.exercise06.javarmi.database.DataBaseClient /tmp/database-stub.bin

If the server used the default database-stub.bin name, run:

java -cp "$CP" ismail.exercise06.javarmi.database.DataBaseClient

The client adds these records:

4101  Appen
4102  Ahrensburg
4103  Wedel
4104  Aumühle
4105  Seevetal
4106  Quickborn

It then reads records 4103 and 4107 and requests the database size.

EXPECTED OUTPUT

addRecord(4101) succeeded
addRecord(4102) succeeded
addRecord(4103) succeeded
addRecord(4104) succeeded
addRecord(4105) succeeded
addRecord(4106) succeeded
getRecord(4103) -> Wedel
getRecord(4107) reports that no record exists
getIndex(Wedel) -> 4103
getIndex(Unknown) -> -1
getRecordObj(4103) -> key=4103, value=Wedel
getSize() -> 6

RUNNING THE CLIENT MORE THAN ONCE

The server keeps its in-memory state until the server process stops. Running
the client multiple times is safe for the current implementation because
addRecord replaces the value for an existing key. The database size remains 6.

TEST SUITE

The automated JUnit test suite can be run with:

mvn -Dtest=Exercise06TestSuite test
