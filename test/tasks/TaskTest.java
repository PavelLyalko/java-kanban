package tasks;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Task равны друг другу, если равен их id")
    void taskObjectsEqualsWhenTheirIdEquals() {
        Task firstTask = new Task("First Task", "First Task Description");
        firstTask.setId(1);

        Task secondTask = new Task("Second Task", "Second Task Description");
        secondTask.setId(1);

        assertEquals(secondTask, firstTask);
    }
}