import manager.TaskManager;
import tasks.Task;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        Map<String, String> test = new HashMap<>();

        test.put("1", "10");
        test.put("2", "20");
        test.put("3", "30");

        System.out.println(test.get(1));

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