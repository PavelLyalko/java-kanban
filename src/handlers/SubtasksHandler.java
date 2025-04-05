package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import tasks.Subtask;
import com.google.gson.GsonBuilder;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] paths = path.split("/");

        switch (method) {
            case "POST":
                createSubtask(exchange);
                break;
            case "GET":
                if (paths.length == 2) {
                    getSubtask(exchange);
                } else if (paths.length == 3) {
                    getSubtaskById(exchange, paths[2]);
                }
                break;
            case "DELETE":
                deleteSubtaskById(exchange, paths[2]);
                break;
            default:
        }
    }

    private void getSubtaskById(HttpExchange exchange, String id) throws IOException {
        Subtask subtask = taskManager.getSubtaskById(Integer.parseInt(id));

        if (subtask != null) {
            String response = gson.toJson(taskManager.getSubtaskById(Integer.parseInt(id)));
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, "Subtask", id);
        }
    }

    private void createSubtask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Subtask subtask = gson.fromJson(json, Subtask.class);
        String response;

        try {
            if (subtask.getId() == null) {
                taskManager.add(subtask);
                response = "Subtask успешно добавлен.";
            } else {
                taskManager.update(subtask);
                response = "Subtask успешно обновлен.";
            }
            sendText(exchange, response);
        } catch (TimeIntersectionException exp) {
            sendHasInteractions(exchange, exp);
        } finally {
            inputStream.close();
        }
    }

    private void getSubtask(HttpExchange exchange) throws IOException {
        sendText(exchange, gson.toJson(taskManager.getSubtasks()));
    }

    private void deleteSubtaskById(HttpExchange exchange, String id) throws IOException {
        taskManager.deleteSubtask(Integer.parseInt(id));
        sendText(exchange, "Subtask успешно удален.");
    }
}
