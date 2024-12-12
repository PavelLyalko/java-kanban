package tasks;

import enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class TaskTest {

    @Test
    @DisplayName("Проверяем, что экземпляры класса Task равны друг другу, если равен их id")
    void taskObjectsEqualsWhenTheirIdEquals() {
        Task firstTask = new Task("First Task", "First Task Description", Status.NEW);
        firstTask.setId(1);

        Task secondTask = new Task("Second Task", "Second Task Description", Status.NEW);
        secondTask.setId(1);

        Assertions.assertEquals(secondTask, firstTask);
    }





}