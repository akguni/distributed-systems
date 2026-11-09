package ismail.exercise03.database;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Test
    void acceptsOnlyOneConcurrentInsertForAnIndex() throws Exception {
        final InMemoryDatabase database = new InMemoryDatabase();
        final ExecutorService workers = Executors.newFixedThreadPool(8);
        try {
            final List<Callable<Boolean>> attempts = java.util.stream.IntStream
                    .range(0, 32)
                    .mapToObj(index -> (Callable<Boolean>) () ->
                            database.add(4103, "record-" + index))
                    .toList();

            final long successfulAdds = workers.invokeAll(attempts).stream()
                    .filter(future -> {
                        try {
                            return future.get();
                        } catch (Exception exception) {
                            throw new AssertionError(exception);
                        }
                    })
                    .count();

            assertEquals(1, successfulAdds);
            assertEquals(1, database.size());
        } finally {
            workers.shutdownNow();
        }
    }
}
