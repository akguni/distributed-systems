package flynn.HA6.code.server;

import flynn.HA6.code.common.DBResult;
import flynn.HA6.code.common.IDataBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataBaseImpl implements IDataBase { // die echte datenbank, läuft nur auf dem server

    private final Map<Integer, String> records = new ConcurrentHashMap<>(); // index -> eintrag

    @Override
    public String getRecord(int index) {
        return records.get(index); // null, wenn es den eintrag nicht gibt
    }

    @Override
    public void addRecord(int index, String record) {
        records.put(index, record);
    }

    @Override
    public int getSize() {
        return records.size();
    }

    @Override
    public int getIndex(String record) {
        for (Map.Entry<Integer, String> entry : records.entrySet()) { // geht alle einträge durch
            if (entry.getValue().equals(record)) {
                return entry.getKey();
            }
        }
        return -1; // kein eintrag mit diesem text
    }

    @Override
    public DBResult getRecordObj(int index) {
        String record = records.get(index);
        if (record == null) {
            return null;
        }
        DBResult result = new DBResult();
        result.setKey(index);
        result.setValue(record);
        return result; // wird als kopie zum client geschickt
    }
}
