package manager;

import exceptions.TimeIntersectionException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void add(Task task) throws TimeIntersectionException;

    void add(Epic epic);

    void add(Subtask subtask) throws TimeIntersectionException;

    void update(Task task) throws TimeIntersectionException;

    void update(Epic epic);

    void update(Subtask subtask) throws TimeIntersectionException;

    void clearTasks();

    void clearSubtasks();

    void clearEpics();

    void deleteTask(Integer id);

    void deleteEpic(Integer id);

    void deleteSubtask(Integer id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Subtask> getEpicSubtasks(int id);

    HistoryManager getHistoryManager();

    void resetId();

    List<Task> getHistory();

    void clearAll();

    List<Task> getPrioritizedTasks();
}
