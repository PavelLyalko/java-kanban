package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import enums.Type;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

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

class TasksHandlerTest {
    private TaskManager taskManager;
    private TasksHandler tasksHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream responseBytes;

    @BeforeEach
    void setUp() {
        taskManager = mock(TaskManager.class);
        tasksHandler = new TasksHandler(taskManager);
        exchange = mock(HttpExchange.class);
        responseBytes = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Проверяет получение Task по id.")
    void testGetTaskById() throws IOException {
        Task task = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        String taskJson = "{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"}";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getTaskById(1)).thenReturn(task);
        when(exchange.getRequestURI()).thenReturn(URI.create("/tasks/1"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        tasksHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(taskJson, response);
    }

    @Test
    @DisplayName("Проверяет создание Task.")
    void testCreateTask() throws IOException, TimeIntersectionException {
        String json = "{\"name\":\"Test Task\"}";
        Headers headers = new Headers();
        InputStream inputStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestURI()).thenReturn(URI.create("/tasks"));
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestBody()).thenReturn(inputStream);
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        tasksHandler.handle(exchange);

        verify(taskManager).add(any(Task.class));
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Task успешно добавлена.", response);
    }

    @Test
    @DisplayName("Проверяет получение всех Task.")
    void testGetTasks() throws IOException {
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        List<Task> tasks = Arrays.asList(task1, task2);
        String tasksJson = "[{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"},{\"name\":\"task2\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-02-01T01:01:01\"}]";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getTasks()).thenReturn(tasks);
        when(exchange.getRequestURI()).thenReturn(URI.create("/tasks"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        tasksHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(tasksJson, response);
    }

    @Test
    @DisplayName("Проверяет удаление Task по id.")
    void testDeleteTaskById() throws IOException {
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("/tasks/1"));
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        tasksHandler.handle(exchange);

        verify(taskManager).deleteTask(1);
        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals("Task успешно удалена.", response);
    }
}
