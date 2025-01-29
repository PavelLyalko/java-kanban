package manager;

import tasks.Task;
import utils.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private HistoryLinkedHashMap<Integer, Task> historyList = new HistoryLinkedHashMap<>();

    private Map<Integer, Node> historyMap= new HashMap<>();

    public Map<Integer, Node> getHistoryMap() {
        return historyMap;
    }

    @Override
    public void add(Task task) {
        historyList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        historyList.remove(historyMap.get(id).getIndex());
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }

    class HistoryLinkedHashMap<K,V> extends LinkedHashMap<K,V> {
        public void linkLast(Task task) {
            if (historyMap.containsKey(task.getId())) {
                remove(historyMap.get(task.getId()).getIndex());
                historyList.put(task.getId(), task);
                historyMap.put(task.getId(), new Node<>(task.getId(),null, task, null));
            } else {
                historyList.put(task.getId(), task);
                historyMap.put(task.getId(), new Node<>(task.getId(), null, task, null));
            }
        }

        public List<Task> getTasks() {
            return new ArrayList<>(historyList.values());
        }
    }

}
