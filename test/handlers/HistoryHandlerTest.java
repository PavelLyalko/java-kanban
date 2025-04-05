package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import enums.Type;
import manager.HistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HistoryHandlerTest {
    private HistoryHandler historyHandler;
    private HttpExchange exchange;
    private ByteArrayOutputStream responseBytes;
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = mock(HistoryManager.class);
        historyHandler = new HistoryHandler(historyManager);
        exchange = mock(HttpExchange.class);
        responseBytes = new ByteArrayOutputStream();
    }

    @Test
    @DisplayName("Полуение истории.")
    void testGetHistory() throws IOException {
        Task task = new Task(Type.TASK, "task1", "dasdasd", 60, LocalDateTime.of(2025, 1, 1, 1, 1, 1));
        Headers headers = new Headers();
        String historyJson = "[{\"name\":\"task1\",\"description\":\"dasdasd\",\"status\":\"NEW\",\"type\":\"TASK\",\"duration\":60,\"startTime\":\"2025-01-01T01:01:01\"}]";
        when(exchange.getResponseHeaders()).thenReturn(headers);
        when(historyManager.getHistory()).thenReturn(List.of(task));
        when(exchange.getRequestURI()).thenReturn(URI.create("/history"));
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getResponseBody()).thenReturn(responseBytes);

        historyHandler.handle(exchange);

        String response = responseBytes.toString(StandardCharsets.UTF_8);
        assertEquals(historyJson, response);
    }
}
