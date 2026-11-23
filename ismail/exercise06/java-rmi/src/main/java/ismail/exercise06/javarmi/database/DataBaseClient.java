package ismail.exercise06.javarmi.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataBaseClient {
    private static final String DEFAULT_STUB_FILE =
            "database-stub.bin";

    private DataBaseClient() {
    }

    public static void main(String[] args) throws Exception {
        final String stubFile = args.length == 1
                ? args[0]
                : DEFAULT_STUB_FILE;
        final DataBase database = readStubFromFile(stubFile);
        runExerciseScenario(database);
    }

    static void runExerciseScenario(DataBase database) throws Exception {
        final int[] indexes = {4101, 4102, 4103, 4104, 4105, 4106};
        final String[] values = {
                "Appen", "Ahrensburg", "Wedel",
                "Aumühle", "Seevetal", "Quickborn"
        };

        for (int i = 0; i < indexes.length; i++) {
            database.addRecord(indexes[i], values[i]);
            System.out.println("addRecord(" + indexes[i] + ") succeeded");
        }

        final String record4103 = database.getRecord(4103);
        final String record4107 = database.getRecord(4107);
        System.out.println(record4103 == null
                ? "getRecord(4103) reports that no record exists"
                : "getRecord(4103) -> " + record4103);
        System.out.println(record4107 == null
                ? "getRecord(4107) reports that no record exists"
                : "getRecord(4107) -> " + record4107);
        System.out.println("getIndex(Wedel) -> " + database.getIndex("Wedel"));
        System.out.println("getIndex(Unknown) -> " + database.getIndex("Unknown"));
        final DBResult recordObject = database.getRecordObj(4103);
        System.out.println(recordObject == null
                ? "getRecordObj(4103) reports that no record exists"
                : "getRecordObj(4103) -> key=" + recordObject.getKey()
                        + ", value=" + recordObject.getValue());
        System.out.println("getSize() -> " + database.getSize());
    }

    private static DataBase readStubFromFile(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(fis);
        DataBase remoteObj = (DataBase) in.readObject();
        in.close();
        return remoteObj;
    }
}
