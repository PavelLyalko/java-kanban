package tasks;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Subtask равны друг другу, если равен их id")
    void subtaskObjectsEqualsWhenTheirIdEquals() {
        Epic epic = new Epic("First Epic", "First Epic Description", new ArrayList<>());
        Subtask firstSubtask = new Subtask("First Subtask", "First Subtask Description", epic.getId());
        firstSubtask.setId(1);

        Subtask secondSubtask = new Subtask("Second Subtask", "Second Subtask Description", epic.getId());
        secondSubtask.setId(1);

        assertEquals(firstSubtask, secondSubtask);
    }

    @Test
    @DisplayName("Проверяем, что объект Subtask нельзя сделать своим же эпиком")
    void checkSubtaskCantBeEpic() {
        TaskManager manager = new InMemoryTaskManager();
        Epic epic = new Epic("First Epic", "First Epic Description", new ArrayList<>());
        manager.add(epic);
        Subtask subtask = new Subtask("Subtask name", "Subtask Description", epic.getId());
        manager.add(subtask);

        Subtask newSubtask = new Subtask("Second Subtask", "Second Subtask Description", subtask.getId());
        manager.add(newSubtask);

        assertEquals(1, manager.getSubtasks().size());
    }
}