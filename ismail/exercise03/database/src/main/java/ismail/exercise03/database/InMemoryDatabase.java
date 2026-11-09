package ismail.exercise03.database;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

final class InMemoryDatabase {
    private final Map<Integer, String> records = new ConcurrentHashMap<>();

    boolean add(int index, String record) {
        return records.putIfAbsent(index, record) == null;
    }

    Optional<String> get(int index) {
        return Optional.ofNullable(records.get(index));
    }

    int size() {
        return records.size();
    }
}
