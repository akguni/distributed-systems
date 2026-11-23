package ismail.exercise06.javarmi.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataBaseImplTest {
    @Test
    void clientScenarioUsesAllSixRecords() throws Exception {
        final DataBaseImpl database = new DataBaseImpl();

        DataBaseClient.runExerciseScenario(database);

        assertEquals("Wedel", database.getRecord(4103));
        assertNull(database.getRecord(4107));
        assertEquals(4103, database.getIndex("Wedel"));
        assertEquals(-1, database.getIndex("Unknown"));
        assertEquals(4103, database.getRecordObj(4103).getKey());
        assertEquals("Wedel", database.getRecordObj(4103).getValue());
        assertNull(database.getRecordObj(4107));
        assertEquals(6, database.getSize());
    }

    @Test
    void exerciseScenarioStoresSixRecordsAndReadsRequestedKeys() throws Exception {
        final DataBaseImpl database = new DataBaseImpl();
        final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
        final String[] values = {
                "Appen", "Ahrensburg", "Wedel",
                "Aumühle", "Seevetal", "Quickborn"
        };

        for (int i = 0; i < indexes.length; i++) {
            database.addRecord(indexes[i], values[i]);
        }

        assertEquals("Wedel", database.getRecord(4103));
        assertNull(database.getRecord(4107));
        assertEquals(6, database.getSize());
    }

    @Test
    void storesReadsAndCountsRecords() throws Exception {
        final DataBaseImpl database = new DataBaseImpl();

        database.addRecord(4103, "Wedel");

        assertEquals("Wedel", database.getRecord(4103));
        assertNull(database.getRecord(4107));
        assertEquals(1, database.getSize());
    }

    @Test
    void allowsARecordToReplaceAnExistingKey() throws Exception {
        final DataBaseImpl database = new DataBaseImpl();

        database.addRecord(4103, "Wedel");
        database.addRecord(4103, "Aumühle");

        assertEquals("Aumühle", database.getRecord(4103));
        assertEquals(1, database.getSize());
    }
}
