import enums.Status;
import manager.HistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("First Epic", "First Epic Description", Status.NEW, new ArrayList<>());
        taskManager.add(epic1);
        taskManager.add(new Subtask("First SubTask", "First SubTask Description", Status.NEW, epic1.getId()));
        taskManager.add(new Task("First Task", "First Task Description", Status.NEW));
        taskManager.add(new Task("Second Task", "Second Task Description", Status.NEW));

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}