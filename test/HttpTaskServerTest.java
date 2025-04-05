import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private HttpServer mockHttpServer;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        httpTaskServer = new HttpTaskServer(0);
        mockHttpServer = mock(HttpServer.class);
        when(mockHttpServer.createContext(anyString(), any())).thenReturn(null);
        Field field = HttpTaskServer.class.getDeclaredField("httpServer");
        field.setAccessible(true);
        field.set(httpTaskServer, mockHttpServer);
    }

    @AfterEach
    void tearDown() {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
    }

    @Test
    @DisplayName("Проверяет иницилиализацию сервера.")
    void testInitialization() {
        httpTaskServer.createContext();
        verify(mockHttpServer, times(5)).createContext(anyString(), any());
    }

    @Test
    @DisplayName("Проверяет загрузку сервера.")
    void testStart() {
        httpTaskServer.start();
        verify(mockHttpServer, times(1)).start();
    }

    @Test
    @DisplayName("Проверяет останвку сервера.")
    void testStop() {
        httpTaskServer.stop();
        verify(mockHttpServer, times(1)).stop(0);
    }
}