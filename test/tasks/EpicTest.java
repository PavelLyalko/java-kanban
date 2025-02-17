package tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EpicTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Epic равны друг другу, если равен их id")
    void epicObjectsEqualsWhenTheirIdEquals() {
        Epic firstEpic = new Epic("First Epic", "First Epic Description", new ArrayList<>());
        firstEpic.setId(1);

        Epic secondEpic = new Epic("Second Epic", "Second Epic Description", new ArrayList<>());
        secondEpic.setId(1);

        assertEquals(firstEpic, secondEpic);
    }

    @Test
    @DisplayName("Проверяем, что объект Epic нельзя добавить в самого себя в виде подзадачи;")
    void test() {
        Epic epic = new Epic("Epic", "Epic Description", new ArrayList<>());
        epic.setId(1);
        epic.addSubtaskId(epic.getId());
        assertTrue(epic.getSubtasksId().isEmpty());
    }

}