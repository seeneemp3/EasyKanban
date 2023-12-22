package TaskTracker.http.handler;

import TaskTracker.manager.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PrioritizedTasksHandler  implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;


    public PrioritizedTasksHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }
    private void writeResponse(HttpExchange ex, String res) throws IOException {
        try (OutputStream os = ex.getResponseBody()) {os.write(res.getBytes(UTF));}
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        String method = ex.getRequestMethod();
        if (method.equals("GET")) {
            URI uri = ex.getRequestURI();
            String path = uri.getPath();
            if (path.equals("/tasks/") && uri.getQuery() == null) {
                String res = gson.toJson(taskManager.getPrioritizedTasks());
                ex.sendResponseHeaders(200, res.length());
                writeResponse(ex, res);
            }
        } else {
            ex.sendResponseHeaders(401, 0);
            writeResponse(ex, "Invalid request method");
        }

}

}
