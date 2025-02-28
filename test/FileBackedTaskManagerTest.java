import enums.Type;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    @Test
    @DisplayName("Проверяет коректность работы файлового менеджера.")
    void testSuccessWorkFileManager() throws IOException {
        File file = File.createTempFile("test", ".txt");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        Task task1 = new Task(Type.TASK, "task1", "dasdasd");
        Task task2 = new Task(Type.TASK, "task2", "dasdasd");
        fileBackedTaskManager.clearAll();
        fileBackedTaskManager.add(task1);
        fileBackedTaskManager.add(task2);

        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, newFileBackedTaskManager.getTasks().size());
    }

    @Test
    @DisplayName("Сохранение нескольких задач.")
    void successSaveTaskTest() throws IOException {
        File file = File.createTempFile("test", ".txt");
        Task task1 = new Task(1, Type.TASK, "task1", "dasdasd");
        Task task2 = new Task(2, Type.TASK, "task2", "dasdasd");
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
        System.out.println(fileBackedTaskManager.getTasks());
        assertEquals(2, fileBackedTaskManager.getEpics().size());
        assertEquals(2, fileBackedTaskManager.getTasks().size());
    }


}
