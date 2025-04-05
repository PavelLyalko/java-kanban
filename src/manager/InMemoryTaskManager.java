package manager;

import enums.Status;
import exceptions.TimeIntersectionException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.TaskComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static manager.Managers.getDefaultHistory;

public class InMemoryTaskManager implements TaskManager {
    protected static Map<Integer, Task> tasks = new HashMap<>();
    protected static Map<Integer, Epic> epics = new HashMap<>();
    protected static Map<Integer, Subtask> subtasks = new HashMap<>();
    protected Set<Task> prioritizedTasks = new TreeSet<>(new TaskComparator());
    private static int nextId = 1;

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private HistoryManager historyManager = getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void add(Task task) throws TimeIntersectionException {
        if (tasks.containsKey(task.getId())) {
            System.out.println("Не уникальный ID!");
        } else {
            if (checkTime(task)) {
                task.setId(nextId++);
                task.setStatus(Status.NEW);
                tasks.put(task.getId(), task);
                addPrioritizedTasks(task);
            } else {
                throw new TimeIntersectionException("Время выполнения задачи уже занято!");
            }
        }
    }

    @Override
    public void add(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            System.out.println("Не уникальный ID!");
        } else {
            epic.setId(nextId++);
            epic.setStatus(Status.NEW);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void add(Subtask subtask) throws TimeIntersectionException {
        if (subtasks.containsKey(subtask.getId())) {
            System.out.println("Не уникальный ID!");
        } else {
            if (subtasks.containsKey(subtask.getEpicId())) {
                return;
            }
            if (epics.containsKey(subtask.getEpicId())) {
                if (checkTime(subtask)) {
                    subtask.setId(nextId++);
                    subtask.setStatus(Status.NEW);

                    subtasks.put(subtask.getId(), subtask);

                    updateStartTime(subtask);
                    updateDuration(subtask);
                    epics.get(subtask.getEpicId()).getEndTime();

                    epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());

                    addPrioritizedTasks(subtask);
                } else {
                    throw new TimeIntersectionException("Время выполнения задачи уже занято!");
                }

            } else {
                System.out.println("Не найден Epic для Subtask");
            }
        }
    }

    private void updateDuration(Subtask subtask) {
        if (epics.get(subtask.getEpicId()).getDuration() != null) {
            epics.get(subtask.getEpicId()).setDuration(epics.get(subtask.getEpicId()).getDuration().plus(subtask.getDuration()));
        } else {
            epics.get(subtask.getEpicId()).setDuration(subtask.getDuration());
        }
    }

    private void updateStartTime(Subtask subtask) {
        if (epics.get(subtask.getEpicId()).getStartTime() == null) {
            epics.get(subtask.getEpicId()).setStartTime(subtask.getStartTime());
        } else {
            if (epics.get(subtask.getEpicId()).getStartTime().isAfter(subtask.getStartTime())) {
                epics.get(subtask.getEpicId()).setStartTime(subtask.getStartTime());
            }
        }
    }

    @Override
    public void clearAll() {
        clearTasks();
        clearSubtasks();
        clearEpics();
        setNextId(1);
    }

    @Override
    public void update(Task task) throws TimeIntersectionException {
        if (checkTime(task)) {
            tasks.put(task.getId(), task);
            addPrioritizedTasks(task);
        } else {
            throw new TimeIntersectionException("Время выполнения задачи уже занято!");
        }
    }

    @Override
    public void update(Epic epic) {
        if (epic.getId() == epics.get(epic.getId()).getId() && epic.getSubtasksId().equals(epics.get(epic.getId()).getSubtasksId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void update(Subtask subtask) throws TimeIntersectionException {
        if (checkTime(subtask)) {
            Epic epic = epics.get(subtask.getEpicId());

            switch (subtask.getStatus()) {
                case DONE:
                    boolean isAllSubtasksStatusDone = epic.getSubtasksId().stream()
                            .map(subtasks::get)
                            .allMatch(task -> task.getStatus() == Status.DONE);

                    epic.setStatus(isAllSubtasksStatusDone ? Status.DONE : Status.IN_PROGRESS);
                    break;

                case IN_PROGRESS:
                    epic.setStatus(Status.IN_PROGRESS);
                    break;

                case NEW:
                    boolean isAllSubtasksStatusNew = epic.getSubtasksId().stream()
                            .map(subtasks::get)
                            .allMatch(task -> task.getStatus() == Status.NEW);

                    epic.setStatus(isAllSubtasksStatusNew ? Status.NEW : Status.IN_PROGRESS);
                    break;
            }
            subtasks.put(subtask.getId(), subtask);
            addPrioritizedTasks(subtask);
        } else {
            throw new TimeIntersectionException("Время выполнения задачи уже занято!");
        }
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();

        epics.values()
                .forEach(epic -> epic.getSubtasksId().clear());
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null && epic.getSubtasksId() != null) {
            epic.getSubtasksId()
                    .forEach(subtasks::remove);
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
        return tasks.values().stream()
                .peek(historyManager::add)
                .collect(Collectors.toList());
    }

    @Override
    public List<Epic> getEpics() {
        epics.values()
                .forEach(historyManager::add);
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        subtasks.values()
                .forEach(historyManager::add);

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
    public List<Subtask> getEpicSubtasks(int id) {
        return epics.get(id).getSubtasksId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
    }


    public static void setNextId(int id) {
        nextId = id;
    }

    @Override
    public void resetId() {
        nextId = 1;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private boolean checkTime(Task task) {
        return getPrioritizedTasks().stream()
                .filter(currentTask -> currentTask.getId() != task.getId())
                .allMatch(currentTask -> task.getEndTime().isBefore(currentTask.getStartTime()) || task.getStartTime().isAfter(currentTask.getEndTime()));
    }

}