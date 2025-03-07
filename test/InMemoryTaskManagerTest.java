import enums.Status;
import enums.Type;
import exceptions.TimeIntersectionException;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    @DisplayName("Для подзадач необходимо дополнительно убедиться в наличии связанного эпика.")
    void makingSureThatThereIsACoherentEpic() throws TimeIntersectionException{
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Subtask subtask = new Subtask(Type.SUBTASK, "Subtask name", "Subtask Description", 2, 50, LocalDateTime.now());
        inMemoryTaskManager.add(subtask);

        assertEquals(0, inMemoryTaskManager.getSubtasks().size());
        inMemoryTaskManager.clearAll();
    }

    @Test
    @DisplayName("Проверяем, что если добавить задачу с пересекающимся временем, то сработает валидация и задача не добавится")
    void checkThatAddTaskWithOverlappingTimeTaskWillNotBeAdded() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 12, 0));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 13, 0));
        Task task3 = new Task(Type.TASK, "task2", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 12, 25));
        Task task4 = new Task(Type.TASK, "task2", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 13, 25));

        manager.add(task1);
        manager.add(task2);
        assertThrows(TimeIntersectionException.class, () -> manager.add(task3));
        assertThrows(TimeIntersectionException.class, () -> manager.add(task4));

        System.out.println(manager.getTasks());

        List<Task> list = manager.getPrioritizedTasks();
        assertEquals(2, list.size());
        assertEquals(task1, list.getFirst());
        assertEquals(task2, list.get(1));

        manager.clearAll();
    }

    @Test
    @DisplayName("Проверяем добаление Task")
    void successAddTaskTest() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Task task1 = new Task(1, Type.TASK, "task1", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 12, 0));
        manager.add(task1);
        assertEquals(task1, manager.getTaskById(1));

        manager.clearAll();
    }

    @Test
    @DisplayName("Проверяем добаление Epic")
    void successAddEpicTest() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        assertEquals(epic1, manager.getEpicById(1));

        manager.clearAll();
    }

    @Test
    @DisplayName("Проверяем добаление Subtask")
    void successAddSubtaskTest() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "Subtask name", "Subtask Description", epic1.getId(), 50, LocalDateTime.of(2024, 1, 1, 1, 1, 1, 1));
        manager.add(subtask1);
        System.out.println(manager.getSubtasks());
        assertEquals(subtask1, manager.getSubtasks().get(0));

        manager.clearAll();
    }

    @Test
    @DisplayName("Проверяем коректное обноваление Task")
    void successUpdateTaskTest() throws TimeIntersectionException {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Task task1 = new Task(1, Type.TASK, "task1", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 12, 0));
        manager.add(task1);
        task1.setStatus(Status.DONE);
        manager.update(task1);
        assertEquals(Status.DONE, manager.getTaskById(task1.getId()).getStatus());
    }

    @Test
    @DisplayName("Проверяем коректное обноваление Subtask")
    void successUpdateSubtaskTest() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "Subtask name", "Subtask Description", epic1.getId(), 50, LocalDateTime.of(2024, 1, 1, 1, 1, 1, 1));
        manager.add(subtask1);
        subtask1.setStatus(Status.DONE);
        manager.update(subtask1);
        assertEquals(Status.DONE, manager.getSubtaskById(subtask1.getId()).getStatus());
    }

    @Test
    @DisplayName("Проверяем коректное обноваление Epic")
    void successUpdateEpicTest() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        epic1.setStatus(Status.DONE);
        manager.update(epic1);
        assertEquals(Status.DONE, manager.getEpicById(epic1.getId()).getStatus());
    }

    @Test
    @DisplayName("Проверяем удаление всех Task")
    void successClearTaskTest() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Task task1 = new Task(1, Type.TASK, "task1", "dasdasd", 30, LocalDateTime.of(2025, 1, 1, 12, 0));
        manager.add(task1);
        manager.clearTasks();
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем удаление всех Subtask")
    void successClearSubtaskTest() throws TimeIntersectionException{
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "Subtask name", "Subtask Description", epic1.getId(), 50, LocalDateTime.of(2024, 1, 1, 1, 1, 1, 1));
        manager.add(subtask1);
        manager.clearSubtasks();
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    @DisplayName("Проверяем удаление всех Epic")
    void successClearEpicTest() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.clearAll();
        Epic epic1 = new Epic(Type.EPIC, "Epic", "Epic Description");
        manager.add(epic1);
        manager.clearEpics();
        assertEquals(0, manager.getEpics().size());
    }
}