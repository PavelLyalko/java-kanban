package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    void update(Task task);

    void update(Epic epic);

    void update(Subtask subtask);

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

    List<Task> getEpicSubtasks(int id);

    List<Task> getHistory();

    void clearAll();

}
