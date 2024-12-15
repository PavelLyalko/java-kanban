import enums.Status;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManagerTest {

    @Test
    @DisplayName("Проверяем, что добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.")
    void checkGetHistoryMethodWorkCorrect() {
        TaskManager manager = new InMemoryTaskManager();
        Task firstTask = new Task("First Task", "First Task Description", Status.NEW);
        manager.add(firstTask);

        Task currentTask = manager.getTaskById(1);
        currentTask.setName("Update First Task");
        currentTask.setDescription("Update First Task Description");

        manager.update(currentTask);
        manager.getTaskById(1);

        Assertions.assertNotEquals(manager.getHistory().get(0).getName(),  manager.getTaskById(1).getName());
    }

    @Test
    @DisplayName("Провенряем корректность заполнения истории")
    void checkAddHistoryMethodWorkCorrect() {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Task Description 1", Status.NEW);
        manager.add(task1);
        manager.getTaskById(task1.getId());
        Task task2 = new Task("Task 2", "Task Description 2", Status.NEW);
        manager.add(task2);
        manager.getTaskById(task2.getId());
        Task task3 = new Task("Task 3", "Task Description 3", Status.NEW);
        manager.add(task3);
        manager.getTaskById(task3.getId());
        Task task4 = new Task("Task 4", "Task Description 4", Status.NEW);
        manager.add(task4);
        manager.getTaskById(task4.getId());
        Task task5 = new Task("Task 5", "Task Description 5", Status.NEW);
        manager.add(task5);
        manager.getTaskById(task5.getId());
        Task task6 = new Task("Task 6", "Task Description 6", Status.NEW);
        manager.add(task6);
        manager.getTaskById(task6.getId());
        Task task7 = new Task("Task 7", "Task Description 7", Status.NEW);
        manager.add(task7);
        manager.getTaskById(task7.getId());
        Epic epic1 = new Epic("Epic 1", "Epic Description 1", Status.NEW, new ArrayList<>());
        manager.add(epic1);
        manager.getEpicById(epic1.getId());
        Subtask subtask1 = new Subtask("Subtask 1", "Subtask Description 2", Status.NEW, epic1.getId());
        manager.add(subtask1);
        manager.getSubtaskById(subtask1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Subtask Description 2", Status.NEW, epic1.getId());
        manager.add(subtask2);
        manager.getSubtaskById(subtask2.getId());

        Task newTask = new Task("New Task", "New Task Description", Status.NEW);
        manager.add(newTask);
        manager.getTaskById(newTask.getId());

        Assertions.assertEquals(task2, manager.getHistory().get(0));
        Assertions.assertEquals(newTask, manager.getHistory().get(9));
    }

}
