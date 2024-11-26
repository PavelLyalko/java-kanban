import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int nextId = 1;

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

            epics.get(id).getSubtasksId().add(subtask.getId());
        } else {
            System.out.println("Не найден Epic для Subtask.");
        }
    }

    public void update(Task task, Status status) {
        task.setStatus(status);
        tasks.put(task.getId(), task);
    }

    public void update(Epic epic, Status status) {
        if (status == Status.IN_PROGRESS) {
            epic.setStatus(status);
            epics.put(epic.getId(), epic);
        } else if (Status.DONE == status) {
            boolean flag = false;

            if (epic.getSubtasksId() != null) {
                for (Integer id : epic.getSubtasksId()) {
                    if (subtasks.get(id).getStatus() != Status.DONE) {
                        flag = true;
                    }
                }
            }

            if (!flag) {
                epic.setStatus(Status.DONE);
                epics.put(epic.getId(), epic);
            }
        }
    }

    public void update(Subtask subtask, Status status) {
        if (status == Status.IN_PROGRESS) {
            epics.get(subtask.getEpicId()).setStatus(Status.IN_PROGRESS);
            subtask.setStatus(Status.IN_PROGRESS);
            subtasks.put(subtask.getId(), subtask);
        } else if (Status.DONE == status) {
            subtask.setStatus(Status.DONE);
            subtasks.put(subtask.getId(), subtask);
            boolean flag = false;

            if (epics.get(subtask.getEpicId()).getSubtasksId() != null) {
                for (Integer id : epics.get(subtask.getEpicId()).getSubtasksId()) {
                    if (subtasks.get(id).getStatus() != Status.DONE) {
                        flag = true;
                    }
                }
            }

            if (!flag) {
                epics.get(subtask.getEpicId()).setStatus(Status.DONE);

            }


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

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
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

    public ArrayList<Task> getAllTaskEpics(int id) {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer currentId : epics.get(id).getSubtasksId()) {
            allTasks.add(subtasks.get(currentId));
        }
        return allTasks;
    }

}
