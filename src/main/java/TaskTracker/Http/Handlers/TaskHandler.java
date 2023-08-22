package TaskTracker.Http.Handlers;

import TaskTracker.Managers.TaskManager;
import TaskTracker.Tasks.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TaskHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;


    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> requestGet(exchange);
            case "POST" -> requestPost(exchange);
            case "DELETE" -> requestDelete(exchange);
        }
    }
    private void writeResponse(HttpExchange ex, String res) throws IOException {
        try (OutputStream os = ex.getResponseBody()) {os.write(res.getBytes(UTF));}
    }
    private void requestGet(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        String path = uri.getPath();
        if( path.equals("/tasks/task/") && uri.getQuery() == null  ){
            String res = gson.toJson(taskManager.getAllTasks());
            ex.sendResponseHeaders(200, res.length());
            writeResponse(ex,res);
        }else if ( uri.getQuery() != null){
            String res = gson.toJson(taskManager.getTask(getIdUri(uri)));
            if (res.equals("null")) {
                ex.sendResponseHeaders(400, 0);
            }else {
                ex.sendResponseHeaders(200, res.length());
            }
            writeResponse(ex, res);
        }
    }
    private void requestPost(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        String body = new String(is.readAllBytes(), UTF);
        Task task = gson.fromJson(body, Task.class);
        if (task.getId() != 0) {
            taskManager.updateTask(task);
            ex.sendResponseHeaders(200, 0);
            writeResponse(ex,"Задача обновлена");
        } else {
            try {
                taskManager.addTask(task);
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex,"Задача добавлена");
            }catch (Exception e){
                ex.sendResponseHeaders(402, 0);
                writeResponse(ex,"не удалось добавить");
            }
        }
    }
    private void requestDelete(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        String path = uri.getPath();
        if(path.equals("/tasks/task/") && uri.getQuery() == null){
            taskManager.deleteAllTasks();
            ex.sendResponseHeaders(200, 0);
            writeResponse(ex,"Все задачи удалены");
        }else if ( uri.getQuery() != null){
            try {
                taskManager.deleteTask(getIdUri(uri));
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex,"Задача с ИД " + getIdUri(uri) + " удалена");
            }catch (IllegalArgumentException e){
                ex.sendResponseHeaders(401, 0);
                writeResponse(ex, e.getMessage());
            }
        }
    }
    private int getIdUri(URI uri) {
        String idUri = uri.getQuery();
        return Integer.parseInt(idUri.split("=")[1]);
    }
}
