package manager;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    @DisplayName("Убеждаемся, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров")
    void utilitarianClassAlwaysReturnGoodObjects() {
        Managers managers = new Managers();

        TaskManager taskManager1 = managers.getDefault();
        TaskManager taskManager2 = managers.getDefault();
        TaskManager taskManager3 = managers.getDefault();
        TaskManager taskManager4 = managers.getDefault();
        TaskManager taskManager5 = managers.getDefault();

        Assertions.assertNotNull(taskManager1);
        Assertions.assertNotNull(taskManager2);
        Assertions.assertNotNull(taskManager3);
        Assertions.assertNotNull(taskManager4);
        Assertions.assertNotNull(taskManager5);

    }

    @Test
    @DisplayName("Проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;")
    void checkTaskWithGeneratedIdAndSetterIdDoNotConflict() {
        TaskManager manager = new InMemoryTaskManager();
        Task firstTask = new Task("First Task", "First Task Description", Status.NEW);
        manager.add(firstTask);

        Task secondTask = new Task("Second Task", "Second Task Description", Status.NEW);
        secondTask.setId(1);
        manager.add(secondTask);

        Assertions.assertEquals(1, manager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void checkCorrectTaskFieldWhenAddMap() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task firstTask = new Task("First Task", "First Task Description", Status.NEW);
        firstTask.setId(1);
        taskManager.add(firstTask);

        Assertions.assertEquals(firstTask.getId(), taskManager.getTaskById(1).getId());
        Assertions.assertEquals(firstTask.getName(), taskManager.getTaskById(1).getName());
        Assertions.assertEquals(firstTask.getDescription(), taskManager.getTaskById(1).getDescription());
        Assertions.assertEquals(firstTask.getStatus(), taskManager.getTaskById(1).getStatus());
    }

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
}