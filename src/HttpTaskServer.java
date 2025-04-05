import com.sun.net.httpserver.HttpServer;
import handlers.EpicsHandler;
import handlers.HistoryHandler;
import handlers.PrioritizedHandler;
import handlers.SubtasksHandler;
import handlers.TasksHandler;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int DEFAULT_PORT = 8080;
    private static HttpServer httpServer;
    private final static TaskManager taskManager = Managers.getDefault();
    private final int port;

    public HttpTaskServer(int port) throws IOException {
        this.port = port;
        httpServer = HttpServer.create(new InetSocketAddress(this.port), 0);
        createContext();
    }

    public HttpTaskServer() throws IOException {
        this(DEFAULT_PORT);
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
    }

    public void createContext() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager.getHistoryManager()));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
    }
}