package ismail.exercise03.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class InMemoryDatabase {
    private final Map<Integer, String> records = new HashMap<>();

    boolean add(int index, String record) {
        if (records.containsKey(index)) {
            return false;
        }
        records.put(index, record);
        return true;
    }

    Optional<String> get(int index) {
        return Optional.ofNullable(records.get(index));
    }

    int size() {
        return records.size();
    }
}
