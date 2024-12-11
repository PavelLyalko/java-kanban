package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private Task[] history = new Task[10];

    @Override
    public void add(Task task) {
        if (history[9] != null) {
            for (int i = 1; i <= history.length - 1; i++) {
                history[i - 1] = history[i];
            }
        } else {
            for (int i = 0; i < history.length; i++) {
                if (history[i] == null) {
                    history[i] = task;
                }
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasksList = new ArrayList<>();
        for (int i = 0; i < history.length; i++) {
            tasksList.add(history[i]);
        }
        return tasksList;
    }
}
