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

class EpicsHandlerTest {
    private TaskManager taskManager;
    private HttpTaskServer server;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        taskManager.resetId();
        server = new HttpTaskServer(9085, taskManager);
        client = HttpClient.newHttpClient();
        server.start();
    }

    @AfterEach
    void setDown() {
        server.stop();
    }

    @Test
    @DisplayName("Проверяет получение Epic по id.")
    void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(epic);
        String epicJson = "{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"First Epic\",\"description\":\"First Epic Description\",\"status\":\"NEW\",\"id\":1,\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9085/epics/1"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(epicJson, response.body());
    }

    @Test
    @DisplayName("Проверяет создание Epic.")
    void testCreateEpic() throws IOException, InterruptedException {
        String json = "{\"name\":\"Test Task\"}";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9085/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Epic успешно добавлен.", response.body());
    }

    @Test
    @DisplayName("Проверяет получение всех Epic.")
    void testGetEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic1.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic1.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Epic epic2 = new Epic(Type.EPIC, "Second Epic", "Second Epic Description");
        epic2.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic2.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(epic1);
        taskManager.add(epic2);
        String subtaskJson = "[{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"First Epic\",\"description\":\"First Epic Description\",\"status\":\"NEW\",\"id\":1,\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"},{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"Second Epic\",\"description\":\"Second Epic Description\",\"status\":\"NEW\",\"id\":2,\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"}]";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9085/epics"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(subtaskJson, response.body());
    }

    @Test
    @DisplayName("Проверяет удаление Epic по id.")
    void testDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic(Type.EPIC, "Epic", "Epic Description");
        taskManager.add(epic);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9085/epics/1"))
                .header("Content-Type", "application/json;charset=utf-8")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals("Epic успешно удален.", response.body());
    }

    @Test
    @DisplayName("Проверка получения всех Subtask у Epic")
    void testGetEpicSubtasks() throws IOException, InterruptedException, TimeIntersectionException {
        Epic epic = new Epic(Type.EPIC, "Epic", "Epic Description");
        taskManager.add(epic);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        Subtask subtask2 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        taskManager.add(subtask1);
        taskManager.add(subtask2);
        String subtaskJson = "[{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"id\":2,\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-02-01T01:01:01\"},{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"id\":3,\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"}]";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9085/epics/1/subtasks"))
                .header("Content-Type", "application/json;charset=utf-8")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(subtaskJson, response.body());
    }

}
