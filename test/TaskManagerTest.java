import enums.Type;
import exceptions.TimeIntersectionException;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static manager.Managers.getDefault;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    @Test
    @DisplayName("Сохранение нескольких задач.")
    void successSaveTaskTest() throws IOException {
        File file = File.createTempFile("test", ".txt");
        Task task1 = new Task(1, Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1,1,1,1,1));
        Task task2 = new Task(2, Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2,1,1,1,1));
        Epic epic3 = new Epic(3, Type.EPIC, "epic3", "epiiiic");
        Epic epic4 = new Epic(4, Type.EPIC, "epic3", "epiiiic");
        BufferedWriter br = new BufferedWriter(new FileWriter(file));
        br.write("id,type,name,status,description,epic \n");
        br.write(task1 + "\n");
        br.write(task2 + "\n");
        br.write(epic3 + "\n");
        br.write(epic4.toString());
        br.close();

        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, fileBackedTaskManager.getEpics().size());
        assertEquals(2, fileBackedTaskManager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;")
    void checkCorrectWorkInMemoryTaskManagerClassOnAddOtherTasksAndGetByIdThemTest() throws TimeIntersectionException {
        TaskManager taskManager = getDefault();
        taskManager.clearAll();
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK,"First SubTask", "First SubTask  Description", epic.getId(), 50, LocalDateTime.now());
        taskManager.add(subtask);
        Task task = new Task(Type.TASK, "First Task", "First Task Description", 60, LocalDateTime.of(2025, 1,1,1,1,1));
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
    void whenSubtaskRemoveThatEpicDoesNotExistHisIdTest() throws TimeIntersectionException{
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.clearAll();
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description");
        taskManager.add(epic);

        Subtask subtask = new Subtask(Type.SUBTASK,"First SubTask", "First SubTask  Description", epic.getId(), 50, LocalDateTime.now());
        taskManager.add(subtask);

        assertEquals(1, epic.getSubtasksId().size());

        taskManager.deleteSubtask(subtask.getId());

        assertEquals(0, epic.getSubtasksId().size());
        taskManager.clearAll();
    }

    @Test
    @DisplayName("Проверяем корректность работы приоретизации задач")
    void checkCorrectnessOfPrioritizationOfTasks() throws TimeIntersectionException{
        TaskManager taskManager = new InMemoryTaskManager();
        taskManager.clearAll();
        Task firstTask = new Task(Type.TASK,"First Task", "First Task Description", 60, LocalDateTime.of(2025,1,1,13,0));
        taskManager.add(firstTask);

        Task secondTask = new Task(Type.TASK,"Second Task", "Second Task Description", 60, LocalDateTime.of(2025,1,1,11,0));
        taskManager.add(secondTask);

        List<Task> list = taskManager.getPrioritizedTasks();

        assertEquals(firstTask, list.get(1));
        assertEquals(secondTask, list.get(0));

        taskManager.clearAll();
    }
}
