package tasks;

import enums.Status;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class EpicTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Epic равны друг другу, если равен их id")
    void epicObjectsEqualsWhenTheirIdEquals() {
        Epic firstEpic = new Epic("First Epic", "First Epic Description", Status.NEW, new ArrayList<>());
        firstEpic.setId(1);

        Epic secondEpic = new Epic("Second Epic", "Second Epic Description", Status.NEW, new ArrayList<>());
        secondEpic.setId(1);

        Assertions.assertEquals(firstEpic, secondEpic);
    }

    @Test
    @DisplayName("Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи;")
    void test() {
        Epic epic = new Epic("Epic", "Epic Description", Status.NEW, new ArrayList<>());
        epic.setId(1);
        epic.addSubtaskId(epic.getId());
        Assertions.assertTrue(epic.getSubtasksId().isEmpty());
    }

}