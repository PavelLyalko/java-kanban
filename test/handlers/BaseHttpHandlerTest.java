package handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import exceptions.TimeIntersectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BaseHttpHandlerTest {
    private HttpExchange mockHttpExchange;
    private ByteArrayOutputStream outputStream;
    private BaseHttpHandler baseHttpHandler;

    @BeforeEach
    void setUp() {
        mockHttpExchange = mock(HttpExchange.class);
        outputStream = new ByteArrayOutputStream();
        baseHttpHandler = new BaseHttpHandler();
    }

    @Test
    @DisplayName("Проверяем успешную отправку ответа.")
    void testSendText() throws IOException {
        String text = "Test message";
        byte[] expectedResponse = text.getBytes(StandardCharsets.UTF_8);
        Headers headers = new Headers();

        when(mockHttpExchange.getResponseBody()).thenReturn(outputStream);
        when(mockHttpExchange.getResponseHeaders()).thenReturn(headers);

        baseHttpHandler.sendText(mockHttpExchange, text);

        verify(mockHttpExchange).getResponseHeaders();
        verify(mockHttpExchange).sendResponseHeaders(200, expectedResponse.length);
        verify(mockHttpExchange).close();

        assertEquals(text, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Проверка работы метода sendNotFound.")
    void testSendNotFound() throws IOException {
        String text = "Item";
        String id = "123";
        String expectedMessage = text + " с id " + id + " не найден.";
        byte[] expectedResponse = expectedMessage.getBytes(StandardCharsets.UTF_8);
        Headers headers = new Headers();

        when(mockHttpExchange.getResponseBody()).thenReturn(outputStream);
        when(mockHttpExchange.getResponseHeaders()).thenReturn(headers);

        baseHttpHandler.sendNotFound(mockHttpExchange, text, id);

        verify(mockHttpExchange).getResponseHeaders();
        verify(mockHttpExchange).sendResponseHeaders(404, expectedResponse.length);
        verify(mockHttpExchange).close();

        assertEquals(expectedMessage, outputStream.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Проверяем работу метода sendHasInteractions.")
    void testSendHasInteractions() throws IOException {
        TimeIntersectionException mockException = mock(TimeIntersectionException.class);
        String exceptionMessage = "Intersection error";
        byte[] expectedResponse = exceptionMessage.getBytes(StandardCharsets.UTF_8);
        Headers headers = new Headers();

        when(mockException.getMessage()).thenReturn(exceptionMessage);
        when(mockHttpExchange.getResponseBody()).thenReturn(outputStream);
        when(mockHttpExchange.getResponseHeaders()).thenReturn(headers);

        baseHttpHandler.sendHasInteractions(mockHttpExchange, mockException);

        verify(mockHttpExchange).getResponseHeaders();
        verify(mockHttpExchange).sendResponseHeaders(406, expectedResponse.length);
        verify(mockHttpExchange).close();

        assertEquals(exceptionMessage, outputStream.toString(StandardCharsets.UTF_8));
    }
}
