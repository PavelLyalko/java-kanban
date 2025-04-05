package handlers;

import enums.Type;
import exceptions.TimeIntersectionException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TasksHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        taskManager.clearTasks();
        taskManager.resetId();
        server = new HttpTaskServer(9090, taskManager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @AfterEach
    void setDown() {
        server.stop();
        taskManager.clearTasks();
    }

    @Test
    @DisplayName("Проверяет создание Task.")
    void testCreateTask() throws IOException, InterruptedException {
        String taskJson = "{\"name\":\"Test Task\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/tasks"))
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Task успешно добавлена.", response.body());
    }

    @Test
    @DisplayName("Проверяет получение всех Task.")
    void testGetTasks() throws IOException, TimeIntersectionException, InterruptedException {
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        taskManager.add(task1);
        taskManager.add(task2);
        String tasksJson = "[{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"id\":1,\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"},{\"name\":\"task2\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"id\":2,\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-02-01T01:01:01\"}]";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/tasks"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(tasksJson, response.body());
    }

    @Test
    @DisplayName("Проверяет удаление Task по id.")
    void testDeleteTaskById() throws IOException, TimeIntersectionException, InterruptedException {
        Task task = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/tasks/1"))
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Task успешно удалена.", response.body());
    }

    @Test
    @DisplayName("Проверяет получение Task по id.")
    void testGetTaskById() throws IOException, InterruptedException, TimeIntersectionException {
        Task task = new Task(Type.TASK, "Task 1", "Task Description 1", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(task);
        String taskJson = "{\"name\":\"Task 1\",\"description\":\"Task Description 1\",\"status\":\"NEW\",\"id\":1,\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/tasks/1"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(taskJson, response.body());
    }
}
