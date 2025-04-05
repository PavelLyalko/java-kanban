import enums.Type;
import exceptions.TimeIntersectionException;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;

import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    @Test
    @DisplayName("Провенряем корректность заполнения истории")
    void checkAddTestHistoryMethodWorkCorrectTest() throws TimeIntersectionException {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task(Type.TASK, "Task 1", "Task Description 1", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        manager.add(task1);
        manager.getTaskById(task1.getId());
        Task task2 = new Task(Type.TASK, "Task 2", "Task Description 2", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        manager.add(task2);
        manager.getTaskById(task2.getId());
        Task task3 = new Task(Type.TASK, "Task 3", "Task Description 3", 60, LocalDateTime.of(2025, 3, 1, 1, 1, 1));
        manager.add(task3);
        manager.getTaskById(task3.getId());
        Task task4 = new Task(Type.TASK, "Task 4", "Task Description 4", 60, LocalDateTime.of(2025, 4, 1, 1, 1, 1));
        manager.add(task4);
        manager.getTaskById(task4.getId());

        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        assertEquals(task1, manager.getHistory().get(2));
        assertEquals(task2, manager.getHistory().get(3));
        assertEquals(task3, manager.getHistory().get(0));
        assertEquals(task4, manager.getHistory().get(1));
        manager.clearAll();
    }

    @Test
    @DisplayName("Проверяем корректность добавления и удаления просмотра задачи из истории")
    void checkCorrectAddAndRemoveTaskInHistoryTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(Type.TASK, "Task ", "Task Description", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        task.setId(1);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(task.getId());

        assertEquals(0, historyManager.getHistory().size());

    }

    @Test
    @DisplayName("Проверяем коректность добавления задач")
    void checkSuccessAddTaskTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(Type.TASK, "Task 1", "Task Description 1", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        historyManager.add(task1);
        assertEquals(task1, historyManager.getHistory().get(0));

    }

    @Test
    @DisplayName("Проверяем коректность удаления задач")
    void checkSuccesDeleteTaskTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(Type.TASK, "Task 1", "Task Description 1", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        task1.setId(1);
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        assertEquals(0, historyManager.getHistory().size());
    }
}
