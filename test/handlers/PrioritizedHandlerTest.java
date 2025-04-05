package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import enums.Type;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PrioritizedHandlerTest {
    private TaskManager taskManager;
    private PrioritizedHandler prioritizedHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream responseBytes;

    @BeforeEach
    void setUp() {
        taskManager = mock(TaskManager.class);
        prioritizedHandler = new PrioritizedHandler(taskManager);
        exchange = mock(HttpExchange.class);
        responseBytes = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Возварщает приоритетные задачи")
    void testGetPrioritizedTasks() throws IOException {
        Task task1 = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Task task2 = new Task(Type.TASK, "task2", "dasdasd", 60, LocalDateTime.of(2025, 2, 1, 1, 1, 1));
        List<Task> tasks = Arrays.asList(task1, task2);
        String prioritizedJson = "[{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"},{\"name\":\"task2\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-02-01T01:01:01\"}]";
        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(taskManager.getPrioritizedTasks()).thenReturn(tasks);
        when(exchange.getRequestURI()).thenReturn(URI.create("/prioritized"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        prioritizedHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(prioritizedJson, response);
    }
}
