package flynn.HA6.code.client;

import flynn.HA6.code.common.DBResult;
import flynn.HA6.code.common.IDataBase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataBaseClient {

    private static final String STUB_FILE = "flynn/HA6/stub.bin"; // hier hat der server den stub abgelegt

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IDataBase db = readStubFromFile(STUB_FILE); // ab hier laufen alle aufrufe auf db im server

        db.addRecord(4101, "Appen");
        db.addRecord(4102, "Ahrensburg");
        db.addRecord(4103, "Wedel");
        db.addRecord(4104, "Aumühle");
        db.addRecord(4105, "Seevetal");
        db.addRecord(4106, "Quickborn");

        System.out.println("4103: " + db.getRecord(4103));
        System.out.println("4107: " + db.getRecord(4107)); // null, weil es 4107 nicht gibt
        System.out.println("größe: " + db.getSize());

        System.out.println("index von Wedel: " + db.getIndex("Wedel"));
        System.out.println("index von Hamburg: " + db.getIndex("Hamburg")); // -1, weil es Hamburg nicht gibt

        DBResult result = db.getRecordObj(4103); // kommt als kopie vom server
        System.out.println("objekt 4103: " + result.getKey() + " " + result.getValue());
        System.out.println("objekt 4107: " + db.getRecordObj(4107)); // null, weil es 4107 nicht gibt
    }

    private static IDataBase readStubFromFile(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        /* Deserialize stub using Java Serialization */
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(fis); // wandelt die bytes wieder in ein objekt um
        IDataBase remoteObj = (IDataBase) in.readObject();
        in.close();
        return remoteObj;
    }
}
