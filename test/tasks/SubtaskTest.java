package tasks;

import enums.Type;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Subtask равны друг другу, если равен их id")
    void subtaskObjectsEqualsWhenTheirIdEquals() {
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description");
        Subtask firstSubtask = new Subtask(Type.SUBTASK,"First Subtask", "First Subtask Description", epic.getId());
        firstSubtask.setId(1);

        Subtask secondSubtask = new Subtask(Type.SUBTASK,"Second Subtask", "Second Subtask Description", epic.getId());
        secondSubtask.setId(1);

        assertEquals(firstSubtask, secondSubtask);
    }

    @Test
    @DisplayName("Проверяем, что объект Subtask нельзя сделать своим же эпиком")
    void checkSubtaskCantBeEpic() {
        TaskManager manager = new InMemoryTaskManager();
        manager.clearEpics();
        Epic epic = new Epic(Type.EPIC,"First Epic", "First Epic Description");
        manager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK,"Subtask name", "Subtask Description", epic.getId());
        manager.add(subtask);

        Subtask newSubtask = new Subtask(Type.SUBTASK,"Second Subtask", "Second Subtask Description", subtask.getId());
        manager.add(newSubtask);

        assertEquals(1, manager.getSubtasks().size());
    }

}