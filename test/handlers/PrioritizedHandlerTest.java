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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(9095, taskManager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @AfterEach
    void setDown() {
        server.stop();
    }

    @Test
    @DisplayName("Возварщает приоритетные задачи")
    void testGetPrioritizedTasks() throws IOException, TimeIntersectionException, InterruptedException {
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        taskManager.clearAll();
        taskManager.add(task1);
        taskManager.add(task2);

        String prioritizedJson = "[{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"id\":1,\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"},{\"name\":\"task2\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"id\":2,\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-02-01T01:01:01\"}]";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9095/prioritized"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(prioritizedJson, response.body());
    }

}
