package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import enums.Type;
import exceptions.TimeIntersectionException;
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

class SubtasksHandlerTest {
    private TaskManager taskManager;
    private SubtasksHandler subtasksHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream responseBytes;

    @BeforeEach
    void setUp() {
        taskManager = mock(TaskManager.class);
        subtasksHandler = new SubtasksHandler(taskManager);
        exchange = mock(HttpExchange.class);
        responseBytes = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Проверяет получение Subtask по id.")
    void testGetSubtaskById() throws IOException {
        Epic epic = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic.setId(1);
        Subtask subtask = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        String subtaskJson = "{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"}";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getSubtaskById(2)).thenReturn(subtask);
        when(exchange.getRequestURI()).thenReturn(URI.create("/subtask/2"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        subtasksHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(subtaskJson, response);
    }

    @Test
    @DisplayName("Проверяет создание Subtask.")
    void testCreateSubtask() throws IOException, TimeIntersectionException {
        String json = "{\"name\":\"Test Task\"}";
        Headers headers = new Headers();
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestURI()).thenReturn(URI.create("/subtasks"));
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        subtasksHandler.handle(exchange);

        verify(taskManager).add(any(Subtask.class));
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Subtask успешно добавлен.", response);
    }

    @Test
    @DisplayName("Проверяет получение всех Subtask.")
    void testGetSubtasks() throws IOException {
        Epic epic = new Epic(Type.EPIC, "First Epic", "First Epic Description");
        epic.setId(1);
        Subtask subtask1 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Subtask subtask2 = new Subtask(Type.SUBTASK, "First Subtask", "First Subtask Description", epic.getId(), 50, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        List<Subtask> tasks = Arrays.asList(subtask1, subtask2);
        String tasksJson = "[{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-01-01T01:01:01\"},{\"epicId\":1,\"name\":\"First Subtask\",\"description\":\"First Subtask Description\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"duration\":50,\"startTime\":\"2025-02-01T01:01:01\"}]";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getSubtasks()).thenReturn(tasks);
        when(exchange.getRequestURI()).thenReturn(URI.create("/subtasks"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        subtasksHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(tasksJson, response);
    }

    @Test
    @DisplayName("Проверяет удаление Subtask по id.")
    void testDeleteSubtaskById() throws IOException {
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/subtasks/1"));
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        subtasksHandler.handle(exchange);

        verify(taskManager).deleteSubtask(1);
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Subtask успешно удален.", response);
    }
}
