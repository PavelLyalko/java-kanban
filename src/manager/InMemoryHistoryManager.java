package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private HistoryLinkedHashMap<Integer, Task> historyList = new HistoryLinkedHashMap<>();

    private Map<Integer, Task> historyMap = new HashMap<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.remove(historyMap.get(id).getId());
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    class HistoryLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        public void linkLast(Task task) {
            if (historyMap.containsKey(task.getId())) {
                historyList.remove(task.getId());
            }
            historyList.put(task.getId(), task);
            historyMap.put(task.getId(),  task);
        }

        public List<Task> getTasks() {
            return new ArrayList<>(historyList.values());
        }
    }

}
