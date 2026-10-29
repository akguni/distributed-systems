package ismail.exercise03.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryDatabaseTest {
    @Test
    void storesAndReadsRecords() {
        final InMemoryDatabase database = new InMemoryDatabase();

        assertTrue(database.add(4103, "Wedel"));
        assertEquals("Wedel", database.get(4103).orElseThrow());
        assertEquals(1, database.size());
    }

    @Test
    void rejectsDuplicateIndexes() {
        final InMemoryDatabase database = new InMemoryDatabase();

        assertTrue(database.add(4103, "Wedel"));
        assertFalse(database.add(4103, "Other"));
        assertEquals("Wedel", database.get(4103).orElseThrow());
        assertEquals(1, database.size());
    }

    @Test
    void reportsMissingRecords() {
        final InMemoryDatabase database = new InMemoryDatabase();

        assertTrue(database.get(4107).isEmpty());
        assertEquals(0, database.size());
    }
}
