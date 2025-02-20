import enums.Type;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {

    @Test
    @DisplayName("Проверяем, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;")
    void checkCorrectWorkInMemoryTaskManagerClassOnAddOtherTasksAndGetByIdThemTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description", new ArrayList<>());
        taskManager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK,"First SubTask", "First SubTask  Description", epic.getId());
        taskManager.add(subtask);
        Task task = new Task(Type.TASK, "First Task", "First Task Description");
        taskManager.add(task);

        assertEquals(1, taskManager.getTasks().size());
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubtasks().size());

        assertEquals(epic, taskManager.getEpicById(1));
        assertEquals(subtask, taskManager.getSubtaskById(2));
        assertEquals(task, taskManager.getTaskById(3));

    }

    @Test
    @DisplayName("Проверяем что внутри эпиков не должно оставаться неактуальных id подзадач.")
    void whenSubtaskRemoveThatEpicDoesNotExistHisIdTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description", new ArrayList<>());
        taskManager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK,"First SubTask", "First SubTask  Description", epic.getId());
        taskManager.add(subtask);

        assertEquals(1, epic.getSubtasksId().size());

        taskManager.deleteSubtask(subtask.getId());

        assertEquals(0, epic.getSubtasksId().size());
    }
}
