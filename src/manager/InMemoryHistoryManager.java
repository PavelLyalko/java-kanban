package manager;

import tasks.Epic;
import tasks.Subtask;
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
            history[9] = task;
        } else {
            for (int i = 0; i < history.length; i++) {
                if (history[i] == null) {

                    if (task instanceof Subtask) {
                        Subtask subtask = new Subtask(task.getName(), task.getDescription(), task.getStatus(), ((Subtask) task).getEpicId());
                        subtask.setId(task.getId());

                        history[i] = subtask;

                    } else if (task instanceof Epic) {
                        Epic epic = new Epic(task.getName(), task.getDescription(), task.getStatus(), ((Epic) task).getSubtasksId());
                        epic.setId(task.getId());

                        history[i] = epic;
                    } else {
                        Task newTask = new Task(task.getName(), task.getDescription(), task.getStatus());
                        newTask.setId(task.getId());

                        history[i] = newTask;
                    }

                    break;
                }
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasksList = new ArrayList<>();
        for (int i = 0; i < history.length; i++) {
            if (history[i] != null) {
                tasksList.add(history[i]);
            }
        }
        return tasksList;
    }
}
