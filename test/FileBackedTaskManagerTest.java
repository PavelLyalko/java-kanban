import enums.Type;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {

    @Test
    @DisplayName("Сохранения пустого файла.")
    void successSaveFileTest() throws IOException {
        File result = File.createTempFile("text", ".txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.loadFromFile(result);
//        FileBackedTaskManager.loadFromFile(result);
//        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        Assertions.assertNotNull(fileBackedTaskManager.getTasks());
        Assertions.assertNotNull(fileBackedTaskManager.getSubtasks());
        Assertions.assertNotNull(fileBackedTaskManager.getEpics());
    }

    @Test
    @DisplayName("Сохранение нескольких задач.")
    void successSaveTaskTest() throws IOException {
        File result = File.createTempFile("test", ".txt");
        Task task1 = new Task(Type.TASK, "task1", "dasdasd");
        Task task2 = new Task(Type.TASK, "task2", "dasdasd");
        BufferedWriter br = new BufferedWriter(new FileWriter(result));
        br.write(task1 + "\n");
        br.write(task2.toString());
        br.close();
        BufferedReader buffer = new BufferedReader(new FileReader(result));
        assertEquals(task1.toString(), buffer.readLine());
        assertEquals(task2.toString(), buffer.readLine());
        buffer.close();
    }

    @Test
    @DisplayName("Загрузка нескольких задач.")
    void successLoadTaskTest() throws IOException{


        File result = File.createTempFile("test", ".txt");

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();
        fileBackedTaskManager.loadFromFile(result);

        Task task1 = new Task(Type.TASK, "task1", "dasdasd");
        Task task2 = new Task(Type.TASK, "task2", "dasdasd");
        BufferedWriter br = new BufferedWriter(new FileWriter(result));
        br.write(task1 + "\n");
        br.write(task2.toString());
        br.close();
        BufferedReader buffer = new BufferedReader(new FileReader(result));
        fileBackedTaskManager.fromString(buffer.readLine());
        fileBackedTaskManager.fromString(buffer.readLine());
    }

}
