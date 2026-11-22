package ismail.exercise05.jsonrpc.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class InMemoryDatabase {
    private final Map<Integer, String> records = new HashMap<>();

    synchronized boolean add(int index, String record) {
        return records.putIfAbsent(index, record) == null;
    }

    synchronized Optional<String> get(int index) {
        return Optional.ofNullable(records.get(index));
    }

    synchronized int size() {
        return records.size();
    }
}
