import enums.Type;
import manager.FileBackedTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("file.txt"));

        Task firstTask = new Task(1, Type.TASK, "First Task", "First Task Description", 120, LocalDateTime.of(2000, 1, 1, 12, 0));
        fileBackedTaskManager.add(firstTask);
        Task secondTask = new Task(2, Type.TASK, "Second Task", "Second Task Description", 120, LocalDateTime.of(2000, 1, 1, 17, 30));
        fileBackedTaskManager.add(secondTask);

        printAllTasks(fileBackedTaskManager);

        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
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