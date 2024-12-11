package manager;

import enums.Status;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static manager.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;
    HistoryManager historyManager = getDefaultHistory();

    @Override
    public void add(Task task) {
        task.setId(nextId++);
        task.setStatus(Status.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        epic.setStatus(Status.NEW);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void add(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(nextId++);
            subtask.setStatus(Status.NEW);

            subtasks.put(subtask.getId(), subtask);

            epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        } else {
            System.out.println("Не найден Epic для Subtask");
        }
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void update(Subtask subtask) {
        if (subtask.getStatus() == Status.DONE) {
            boolean isAllSubtasksStatusDone = true;

            for (Integer i : epics.get(subtask.getEpicId()).getSubtasksId()) {
                if (subtasks.get(i).getStatus() != Status.DONE) {
                    isAllSubtasksStatusDone = false;
                }
            }

            if (isAllSubtasksStatusDone) {
                epics.get(subtask.getEpicId()).setStatus(Status.DONE);
            } else {
                epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
            }
            subtasks.put(subtask.getId(), subtask);
        } else if (subtask.getStatus() == Status.IN_PROGRESS) {
            epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
            subtasks.put(subtask.getId(), subtask);
        } else if (subtask.getStatus() == Status.NEW) {
            boolean isAllSubtasksStatusNew = true;

            for (Integer i : epics.get(subtask.getEpicId()).getSubtasksId()) {
                if (subtasks.get(i).getStatus() != Status.NEW) {
                    isAllSubtasksStatusNew = false;
                }
            }

            if (isAllSubtasksStatusNew) {
                epics.get(subtask.getEpicId()).setStatus(Status.NEW);
            } else {
                epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
            }
            subtasks.put(subtask.getId(), subtask);
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpicsAndSubtasks() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        if (epics.get(id).getSubtasksId() != null) {
            for (Integer currentId : epics.get(id).getSubtasksId()) {
                subtasks.remove(currentId);
            }
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        List<Integer> listSubtasks = epic.getSubtasksId();
        listSubtasks.remove(id);

        subtasks.remove(id);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public List<Task> getAllTaskEpics(int id) {
        List<Task> allTasks = new ArrayList<>();
        for (Integer currentId : epics.get(id).getSubtasksId()) {
            allTasks.add(subtasks.get(currentId));
        }
        return allTasks;
    }


}