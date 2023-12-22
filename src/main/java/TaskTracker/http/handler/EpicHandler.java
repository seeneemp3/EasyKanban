package TaskTracker.http.handler;

import TaskTracker.manager.TaskManager;
import TaskTracker.task.Epic;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;
    private static final Charset UTF = StandardCharsets.UTF_8;


    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if( path.equals("/tasks/epic/") && uri.getQuery() == null  ){
            String res = gson.toJson(taskManager.getAllEpics());
            ex.sendResponseHeaders(200, res.length());
            writeResponse(ex,res);
        }else if ( uri.getQuery() != null){
            try {
                String res = gson.toJson(taskManager.getEpic(getIdUri(uri)));
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex, res);
            } catch (IllegalArgumentException e) {
                ex.sendResponseHeaders(401, 0);
                writeResponse(ex, e.getMessage());
            }
        }
    }
    private void requestPost(HttpExchange ex) throws IOException {
        InputStream is = ex.getRequestBody();
        String body = new String(is.readAllBytes(), UTF);
        Epic task = gson.fromJson(body, Epic.class);
        if (task.getId() != 0) {
            try{
                taskManager.updateEpic(task);
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex, "Epic updated");
            }catch (Exception e){
                ex.sendResponseHeaders(400, 0);
                writeResponse(ex, e.getMessage());
            }

        } else {
            try {
                taskManager.addEpic(task);
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex, "Epic added");
            } catch (IllegalArgumentException e) {
                ex.sendResponseHeaders(401, 0);
                writeResponse(ex, "Epic was not added");
            }
        }
    }
    private void requestDelete(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        String path = uri.getPath();
        if (path.equals("/tasks/epic/") && uri.getQuery() == null){
            taskManager.deleteAllEpics();
            ex.sendResponseHeaders(200, 0);
            writeResponse(ex, "All epics have been deleted");
        }else if ( uri.getQuery() != null){
            try {
                taskManager.deleteEpic(getIdUri(uri));
                ex.sendResponseHeaders(200, 0);
                writeResponse(ex, "Epic with ID " + getIdUri(uri) + " deleted");
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
