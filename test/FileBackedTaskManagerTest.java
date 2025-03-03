import enums.Type;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Test
    @DisplayName("Проверяет коректность работы файлового менеджера.")
    void testSuccessWorkFileManager() throws IOException {
        File file = File.createTempFile("test", ".txt");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1,1,1,1,1));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2,1,1,1,1));
        fileBackedTaskManager.clearAll();
        fileBackedTaskManager.add(task1);
        fileBackedTaskManager.add(task2);

        FileBackedTaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertEquals(2, newFileBackedTaskManager.getTasks().size());
    }

    @Test
    @DisplayName("Проверяем что выбрасывается исключение, если файла не существует")
    void checkThatAnExceptioIsThrownTheFileDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> FileBackedTaskManager.loadFromFile(new File("tetete.txt")));
    }



}
