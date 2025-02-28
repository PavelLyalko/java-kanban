package tasks;

import enums.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void test() {
        Epic epic = new Epic(Type.EPIC,"Epic", "Epic Description");
        epic.setId(1);
        epic.addSubtaskId(epic.getId());
        assertTrue(epic.getSubtasksId().isEmpty());
    }

}