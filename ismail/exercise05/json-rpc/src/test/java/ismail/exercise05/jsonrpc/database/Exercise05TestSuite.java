package ismail.exercise05.jsonrpc.database;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        InMemoryDatabaseTest.class,
        DatabaseClientServerTest.class
})
public class Exercise05TestSuite {
}
