package manager;

import enums.Type;
import exceptions.TimeIntersectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.LocalDateTime;

import static manager.Managers.getDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagersTest {

    @Test
    @DisplayName("Убеждаемся, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров")
    void utilitarianClassAlwaysReturnGoodObjects() {
        TaskManager taskManager1 = Managers.getDefault();
        TaskManager taskManager2 = Managers.getDefault();
        TaskManager taskManager3 = Managers.getDefault();
        TaskManager taskManager4 = Managers.getDefault();
        TaskManager taskManager5 = Managers.getDefault();

        assertNotNull(taskManager1);
        assertNotNull(taskManager2);
        assertNotNull(taskManager3);
        assertNotNull(taskManager4);
        assertNotNull(taskManager5);
    }

    @Test
    @DisplayName("Проверяем, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;")
    void checkTaskWithGeneratedIdAndSetterIdDoNotConflict() throws TimeIntersectionException {
        TaskManager manager = getDefault();
        manager.clearAll();
        Task firstTask = new Task(Type.TASK, "First Task", "First Task Description", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        manager.add(firstTask);

        Task secondTask = new Task(Type.TASK, "Second Task", "Second Task Description", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        secondTask.setId(1);
        manager.add(secondTask);

        assertEquals(1, manager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем неизменность задачи (по всем полям) при добавлении задачи в менеджер")
    void checkCorrectTaskFieldWhenAddMap() throws TimeIntersectionException {
        TaskManager taskManager = getDefault();
        taskManager.clearAll();

        Task firstTask = new Task(Type.TASK, "First Task", "First Task Description", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        taskManager.add(firstTask);

        assertEquals(firstTask.getId(), taskManager.getTaskById(1).getId());
        assertEquals(firstTask.getName(), taskManager.getTaskById(1).getName());
        assertEquals(firstTask.getDescription(), taskManager.getTaskById(1).getDescription());
        assertEquals(firstTask.getStatus(), taskManager.getTaskById(1).getStatus());
    }

}