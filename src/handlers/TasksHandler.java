package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TimeIntersectionException;
import manager.TaskManager;
import tasks.Task;
import com.google.gson.GsonBuilder;
import utils.DurationAdapter;
import utils.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] paths = path.split("/");

        switch (method) {
            case "POST":
                createTask(exchange);
                break;
            case "GET":
                if (paths.length == 2) {
                    getTasks(exchange);
                } else if (paths.length == 3) {
                    getTaskById(exchange, paths[2]);
                }
                break;
            case "DELETE":
                deleteTaskById(exchange, paths[2]);
                break;
            default:
        }
    }

    private void getTaskById(HttpExchange exchange, String id) throws IOException {
        Task task = taskManager.getTaskById(Integer.parseInt(id));

        if (task != null) {
            String response = gson.toJson(taskManager.getTaskById(Integer.parseInt(id)));
            sendText(exchange, response);
        } else {
            sendNotFound(exchange, "Task", id);
        }
    }

    private void createTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        Task task = gson.fromJson(json, Task.class);
        String response;

        try {
            if (task.getId() == null) {
                taskManager.add(task);
                response = "Task успешно добавлена.";
            } else {
                taskManager.update(task);
                response = "Task успешно обновлена.";
            }
            sendText(exchange,response);
        } catch (TimeIntersectionException exp) {
            sendHasInteractions(exchange,exp);
        } finally {
            inputStream.close();
        }
    }

    private void getTasks(HttpExchange exchange) throws IOException {
        sendText(exchange,gson.toJson(taskManager.getTasks()));
    }

    private void deleteTaskById(HttpExchange exchange, String id) throws IOException {
        taskManager.deleteTask(Integer.parseInt(id));
        sendText(exchange,"Task успешно удалена.");
    }
}
