package tasks;

import enums.Status;
import enums.Type;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Epic равны друг другу, если равен их id")
    void epicObjectsEqualsWhenTheirIdEquals() {
        Epic firstEpic = new Epic(Type.EPIC,"First Epic", "First Epic Description");
        firstEpic.setId(1);

        Epic secondEpic = new Epic(Type.EPIC,"Second Epic", "Second Epic Description");
        secondEpic.setId(1);

        assertEquals(firstEpic, secondEpic);
    }

    @Test
    @DisplayName("Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи;")
    void epicObjectsCanNotAddHimself() {
        Epic epic = new Epic(Type.EPIC,"Epic", "Epic Description");
        epic.setId(1);
        epic.addSubtaskId(epic.getId());
        assertTrue(epic.getSubtasksId().isEmpty());
    }

    @Test
    @DisplayName("Проверяет граничные условия Epic")
    void rechecksEpicBoundaryConditions() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic(Type.EPIC,"Epic", "Epic Description");
        inMemoryTaskManager.add(epic);

        assertEquals(Status.NEW, epic.getStatus());

        Subtask subtask = new Subtask(Type.SUBTASK,"Subtask name", "Subtask Description", epic.getId(), 50, LocalDateTime.of(2024,1,1,1,1,1,1));
        inMemoryTaskManager.add(subtask);

        Subtask newSubtask = new Subtask(Type.SUBTASK,"Second Subtask", "Second Subtask Description", epic.getId(), 50, LocalDateTime.of(2024,2,1,1,1,1,1));
        inMemoryTaskManager.add(newSubtask);

        assertEquals(Status.NEW, epic.getStatus());

        subtask.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.update(subtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        subtask.setStatus(Status.DONE);
        inMemoryTaskManager.update(subtask);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());

        newSubtask.setStatus(Status.DONE);
        inMemoryTaskManager.update(newSubtask);

        assertEquals(Status.DONE, epic.getStatus());
        inMemoryTaskManager.clearAll();
    }

}