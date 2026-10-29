EXERCISE 03
===========

This exercise implements a TCP log server and client using Protocol Buffers.

REQUIREMENTS

Java 25 or newer
Maven

COMPILE

From this directory, run: mvn clean compile

Maven downloads the Protocol Buffers runtime specified in pom.xml.

REGENERATE JAVA CODE AFTER CHANGING THE PROTO FILE

Maven automatically regenerates the Java source from LogMessage.proto during
the generate-sources phase. The generated source is placed under:

target/generated-sources/protobuf/java/

After changing LogMessage.proto, run:

mvn clean compile

Do not keep manually generated protobuf Java files under src/main/java. Maven
uses the protoc version configured in pom.xml, keeping generated code
compatible with the protobuf runtime.

PREPARE THE CLASSPATH

After compiling, create a runtime classpath containing the compiled classes
and Maven dependencies:

mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
CP="target/classes:$(cat target/classpath.txt)"

START THE SERVER

In one terminal, run:

java -cp "$CP" ismail.exercise03.logging.LogServer 8080 log.txt

LogServer [port] [log-file]

The defaults are port 8080 and log.txt.

SEND LOG MESSAGES

In another terminal, prepare the classpath again if necessary:

CP="target/classes:$(cat target/classpath.txt)"
java -cp "$CP" ismail.exercise03.logging.LogClient \
    localhost 8080 INFO "Server started" \
    LogClient exercise03

The client arguments are:

LogClient <host> <port> <severity> <text> [created-by] [location]

Supported severity values are DEBUG, INFO, WARN, ERROR, and FATAL.
The text must be a single line.

For example:

java -cp "$CP" ismail.exercise03.logging.LogClient \
    localhost 8080 ERROR "Something went wrong" \
    LogClient exercise03

The server appends one formatted log entry per line to `log.txt`, for example:

2026-11-08T12:00:00Z [INFO] LogClient@exercise03: Server started

Stop the server with `Ctrl+C`.

MANUAL VERIFICATION

1. Run mvn clean compile.
2. Start LogServer.
3. Send messages with multiple severity levels using LogClient.
4. Open log.txt and verify that each message appears on its own line.
5. Stop and restart the server, then send another message to verify that
   existing log entries are preserved.
