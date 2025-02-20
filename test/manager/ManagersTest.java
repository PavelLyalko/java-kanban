package manager;

import enums.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


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

        assertNotNull(taskManager1);
        assertNotNull(taskManager2);
        assertNotNull(taskManager3);
        assertNotNull(taskManager4);
        assertNotNull(taskManager5);
    }

    @Test
    @DisplayName("Проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;")
    void checkTaskWithGeneratedIdAndSetterIdDoNotConflict() {
        TaskManager manager = new InMemoryTaskManager();
        Task firstTask = new Task(Type.TASK,"First Task", "First Task Description");
        manager.add(firstTask);

        Task secondTask = new Task(Type.TASK,"Second Task", "Second Task Description");
        secondTask.setId(1);
        manager.add(secondTask);

        assertEquals(1, manager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void checkCorrectTaskFieldWhenAddMap() {
        TaskManager taskManager = new InMemoryTaskManager();

        Task firstTask = new Task(Type.TASK,"First Task", "First Task Description");
        taskManager.add(firstTask);

        assertEquals(firstTask.getId(), taskManager.getTaskById(1).getId());
        assertEquals(firstTask.getName(), taskManager.getTaskById(1).getName());
        assertEquals(firstTask.getDescription(), taskManager.getTaskById(1).getDescription());
        assertEquals(firstTask.getStatus(), taskManager.getTaskById(1).getStatus());
    }

}