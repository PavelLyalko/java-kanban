import enums.Type;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @Test
    @DisplayName("Провенряем корректность заполнения истории")
    void checkAddTestHistoryMethodWorkCorrectTest() {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task(Type.TASK,"Task 1", "Task Description 1");
        manager.add(task1);
        manager.getTaskById(task1.getId());
        Task task2 = new Task(Type.TASK,"Task 2", "Task Description 2");
        manager.add(task2);
        manager.getTaskById(task2.getId());
        Task task3 = new Task(Type.TASK,"Task 3", "Task Description 3");
        manager.add(task3);
        manager.getTaskById(task3.getId());
        Task task4 = new Task(Type.TASK,"Task 4", "Task Description 4");
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
    }

    @Test
    @DisplayName("Проверяем корректность добавления и удаления просмотра задачи из истории")
    void checkCorrectAddAndRemoveTaskInHistoryTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(Type.TASK,"Task ", "Task Description");
        task.setId(1);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());

        historyManager.remove(task.getId());

        assertEquals(0, historyManager.getHistory().size());

    }
}
