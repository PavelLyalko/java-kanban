package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import enums.Type;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EpicsHandlerTest {
    private TaskManager taskManager;
    private EpicsHandler epicsHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream responseBytes;

    @BeforeEach
    void setUp() {
        taskManager = mock(TaskManager.class);
        epicsHandler = new EpicsHandler(taskManager);
        exchange = mock(HttpExchange.class);
        responseBytes = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Проверяет получение Epic по id.")
    void testGetEpicById() throws IOException {
        Epic epic = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        String epicJson = "{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"First Epic\",\"description\":\"First Epic Description\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"}";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getEpicById(1)).thenReturn(epic);
        when(exchange.getRequestURI()).thenReturn(URI.create("/epics/1"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        epicsHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(epicJson, response);
    }

    @Test
    @DisplayName("Проверяет создание Epic.")
    void testCreateEpic() throws IOException {
        String json = "{\"name\":\"Test Task\"}";
        Headers headers = new Headers();
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestURI()).thenReturn(URI.create("/epics"));
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        epicsHandler.handle(exchange);

        verify(taskManager).add(any(Epic.class));
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Epic успешно добавлен.", response);
    }

    @Test
    @DisplayName("Проверяет получение всех Epic.")
    void testGetEpics() throws IOException {
        Epic epic1 = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic1.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic1.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Epic epic2 = new Epic(Type.EPIC, "Second Epic", "Second Epic Description");
        epic2.setStartTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        epic2.setEndTime(LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        List<Epic> epics = Arrays.asList(epic1, epic2);
        String subtaskJson = "[{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"First Epic\",\"description\":\"First Epic Description\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"},{\"subtasksId\":[],\"endTime\":\"2025-01-01T01:01:01\",\"name\":\"Second Epic\",\"description\":\"Second Epic Description\",\"status\":\"NEW\",\"type\":\"EPIC\",\"startTime\":\"2025-01-01T01:01:01\"}]";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getEpics()).thenReturn(epics);
        when(exchange.getRequestURI()).thenReturn(URI.create("/epics"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        epicsHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(subtaskJson, response);
    }

    @Test
    @DisplayName("Проверяет удаление Epic по id.")
    void testDeleteEpicById() throws IOException {
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/epics/1"));
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        epicsHandler.handle(exchange);

        verify(taskManager).deleteEpic(1);
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Epic успешно удален.", response);
    }

    @Test
    @DisplayName("Проверка получения всех Subtask у Epic")
    void testGetEpicSubtasks() throws IOException {
        Subtask subtask1 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", 1, 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Subtask subtask2 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", 1, 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        List<Subtask> subtasks = List.of(subtask1, subtask2);
        String subtaskJson = "[{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"},{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"}]";
        Headers headers = new Headers();
        when(taskManager.getEpicSubtasks(1)).thenReturn(subtasks);
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/epics/1/subtasks"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        epicsHandler.handle(exchange);

        verify(taskManager).getEpicSubtasks(1);
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(subtaskJson, response);
    }

}
