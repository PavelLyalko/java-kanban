package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] paths = path.split("/");

        switch (method) {
            case "POST":
                createEpic(exchange);
                break;
            case "GET":
                if (paths.length == 2) {
                    getEpics(exchange);
                } else if (paths.length == 3) {
                    getEpicById(exchange, paths[2]);
                } else if (paths.length == 4) {
                    getEpicSubtasks(exchange, paths[2]);
                }
                break;
            case "DELETE":
                deleteEpicById(exchange, paths[2]);
                break;
            default:
        }
    }

    private void getEpicSubtasks(HttpExchange exchange, String id) throws IOException {
        List<Subtask> subtasks = taskManager.getEpicSubtasks(Integer.parseInt(id));
        sendText(exchange, gson.toJson(subtasks));
    }

    private void getEpicById(HttpExchange exchange, String id) throws IOException {
        Epic epic = taskManager.getEpicById(Integer.parseInt(id));
        if (epic != null) {
            sendText(exchange, gson.toJson(epic));
        } else {
            sendNotFound(exchange, "Epic", id);
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(json, Epic.class);

        if (epic.getId() == null) {
            taskManager.add(epic);
            sendText(exchange, "Epic успешно добавлен.");
        } else {
            taskManager.update(epic);
            sendText(exchange, "Epic успешно обновлен.");
        }
        inputStream.close();
    }

    private void getEpics(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getEpics()));
    }

    private void deleteEpicById(HttpExchange exchange, String id) throws IOException {
        taskManager.deleteEpic(Integer.parseInt(id));
        sendText(exchange, "Epic успешно удален.");
    }
}
