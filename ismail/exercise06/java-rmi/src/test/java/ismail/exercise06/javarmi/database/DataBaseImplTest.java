package ismail.exercise06.javarmi.database;

import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DataBaseImplTest {
    @Test
    void storesReadsAndCountsRecords() throws RemoteException {
        final DataBaseImpl database = new DataBaseImpl();

        database.addRecord(4103, "Wedel");

        assertEquals("Wedel", database.getRecord(4103));
        assertNull(database.getRecord(4107));
        assertEquals(1, database.getSize());
    }

    @Test
    void allowsARecordToReplaceAnExistingKey() throws RemoteException {
        final DataBaseImpl database = new DataBaseImpl();

        database.addRecord(4103, "Wedel");
        database.addRecord(4103, "Aumühle");

        assertEquals("Aumühle", database.getRecord(4103));
        assertEquals(1, database.getSize());
    }
}
