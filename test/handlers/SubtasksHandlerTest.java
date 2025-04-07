package handlers;

import enums.Type;
import exceptions.TimeIntersectionException;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtasksHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        taskManager.resetId();
        server = new HttpTaskServer(9075, taskManager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @AfterEach
    void setDown() {
        server.stop();
        taskManager.clearEpics();
    }

    @Test
    @DisplayName("Проверяет получение Subtask по id.")
    void testGetSubtaskById() throws IOException, TimeIntersectionException, InterruptedException {
        Epic epic = new Epic(1, Type.EPIC, "First Epic", "First Epic Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(subtask);
        String subtaskJson = "{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"id\":2,\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9075/subtasks/2"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(subtaskJson, response.body());
    }

    @Test
    @DisplayName("Проверяет создание Subtask.")
    void testCreateSubtask() throws IOException, InterruptedException {
        String json = "{\"name\":\"Test Task\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9075/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Epic успешно добавлен.", response.body());
    }

    @Test
    @DisplayName("Проверяет получение всех Subtask.")
    void testGetSubtasks() throws IOException, TimeIntersectionException, InterruptedException {
        Epic epic = new Epic(1, Type.EPIC, "First Epic", "First Epic Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Subtask subtask2 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        String tasksJson = "[{\"subtasksId\":[2,3],\"endTime\":\"2025-01-01T02:41:01\",\"name\":\"First Epic\",\"description\":\"First Epic Description\",\"status\":\"NEW\",\"id\":1,\"type\":\"EPIC\",\"duration\":100,\"startTime\":\"2025-01-01T01:01:01\"}]";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9075/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(tasksJson, response.body());
    }

    @Test
    @DisplayName("Проверяет удаление Subtask по id.")
    void testDeleteSubtaskById() throws IOException, TimeIntersectionException, InterruptedException {
        Epic epic = new Epic(1, Type.EPIC, "First Epic", "First Epic Description");
        taskManager.add(epic);
        Subtask subtask = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(subtask);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9075/subtasks/2"))
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Subtask успешно удален.", response.body());
    }
}
