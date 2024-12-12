package tasks;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SubtaskTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Subtask равны друг другу, если равен их id")
    void subtaskObjectsEqualsWhenTheirIdEquals() {
        Epic epic = new Epic("First Epic", "First Epic Description", Status.NEW, new ArrayList<>());
        Subtask firstSubtask = new Subtask("First Subtask", "First Subtask Description", Status.NEW, epic.getId());
        firstSubtask.setId(1);

        Subtask secondSubtask = new Subtask("Second Subtask", "Second Subtask Description", Status.NEW, epic.getId());
        secondSubtask.setId(1);

        Assertions.assertEquals(firstSubtask, secondSubtask);
    }

    @Test
    @DisplayName("Проверяем, что объект Subtask нельзя сделать своим же эпиком")
    void  checkSubtaskCantBeEpic() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("First Epic", "First Epic Description", Status.NEW, new ArrayList<>());
        manager.add(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask Description", Status.NEW, epic.getId());
        manager.add(subtask);

        Subtask newSubtask = new Subtask("Second Subtask", "Second Subtask Description", Status.NEW, subtask.getId());
        manager.add(newSubtask);

        Assertions.assertEquals(1, manager.getSubtasks().size());
    }
}