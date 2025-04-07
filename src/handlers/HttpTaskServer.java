package handlers;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int DEFAULT_PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_PORT), 0);
        createContext();
    }

    public HttpTaskServer(int port, TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        createContext();
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
        new HttpTaskServer(Managers.getDefault()).start();
    }
}