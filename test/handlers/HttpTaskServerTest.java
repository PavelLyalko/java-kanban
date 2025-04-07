package handlers;

import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private HttpClient httpClient;

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer(9025, Managers.getDefault());
        httpClient = HttpClient.newHttpClient();
        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
    }

    @Test
    @DisplayName("Проверяет создание контекста Task")
    public void testTasksEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/tasks"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверяет создание контекста Task")
    public void testSubtasksEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/subtasks"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверяет создание контекста Epic")
    public void testEpicsEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/epics"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверяет создание контекста History")
    public void testHistoryEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/history"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверяет создание контекста Prioritized")
    public void testPrioritizedEndpoint() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/prioritized"))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверка работы сервера, если он запущен")
    public void testStartServer() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9025/history"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            fail("Сервер не запущен");
        }

        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Проверка работы сервера, если он остановлен")
    public void testStopServer() {
        httpTaskServer.stop();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9015/tasks"))
                .build();

        assertThrows(IOException.class, () -> {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        });
    }

}