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

public class InMemoryTaskManagerTest {

    @Test
    @DisplayName("Проверяем, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;")
    void checkCorrectWorkInMemoryTaskManagerClassOnAddOtherTasksAndGetByIdThem() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("First Epic", "First Epic Description", Status.NEW, new ArrayList<>());
        taskManager.add(epic);

        Subtask subtask = new Subtask("First SubTask", "First SubTask  Description", Status.NEW, epic.getId());
        taskManager.add(subtask);

        Task task = new Task("First Task", "First Task Description", Status.NEW);
        taskManager.add(task);

        Assertions.assertEquals(1, taskManager.getTasks().size());
        Assertions.assertEquals(1, taskManager.getEpics().size());
        Assertions.assertEquals(1, taskManager.getSubtasks().size());

        Assertions.assertEquals(epic, taskManager.getEpicById(1));
        Assertions.assertEquals(subtask, taskManager.getSubtaskById(2));
        Assertions.assertEquals(task, taskManager.getTaskById(3));

    }
}
