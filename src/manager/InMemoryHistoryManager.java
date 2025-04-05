package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private HistoryLinkedHashMap<Integer, Task> historyList = new HistoryLinkedHashMap<>();

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
        System.out.println(task);
    }

    @Override
    public void remove(int id) {
        historyList.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    class HistoryLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
        public void linkLast(Task task) {
            if (containsKey(task.getId())) {
                historyList.remove(task.getId());
            }
            historyList.put(task.getId(), task);
        }

        public List<Task> getTasks() {
            return new ArrayList<>(historyList.values());
        }
    }

}
