package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    public void add(Task task) {
        task.setId(nextId++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    public void add(Subtask subtask, int id) {
        if (epics.containsKey(id)) {
            subtask.setId(nextId++);
            subtask.setStatus(Status.NEW);
            subtask.setEpicId(id);

            subtasks.put(subtask.getId(), subtask);

            epics.get(id).addSubtask(subtask.getId());
        } else {
            System.out.println("Не найден tasks.Epic для tasks.Subtask.");
        }
    }

    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void update(Subtask subtask) {
        if (subtask.getStatus() == Status.DONE) {
            boolean flag = true;
            for (Integer i : epics.get(subtask.getEpicId()).getSubtasksId()) {
                if (subtasks.get(i).getStatus() != Status.DONE) {
                    flag = false;
                }
            }
            if (flag) {
                epics.get(subtask.getEpicId()).setStatus(Status.DONE);
            }
        } else if (subtask.getStatus() == Status.IN_PROGRESS) {
            epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
        }
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpicsAndSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        if (epics.get(id).getSubtasksId() != null) {
            for (Integer currentId : epics.get(id).getSubtasksId()) {
                subtasks.remove(currentId);
            }
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        subtasks.remove(id);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public List<Task> getAllTaskEpics(int id) {
        List<Task> allTasks = new ArrayList<>();
        for (Integer currentId : epics.get(id).getSubtasksId()) {
            allTasks.add(subtasks.get(currentId));
        }
        return allTasks;
    }
}