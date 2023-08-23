package TaskTracker.Http.Handlers;

import TaskTracker.Managers.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;
    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/tasks/history/") && uri.getQuery() == null) {
            String response = gson.toJson(taskManager.getHistory());
            exchange.sendResponseHeaders(200, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes(UTF));
            }
        }
    }
}
