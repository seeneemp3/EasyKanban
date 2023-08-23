package TaskTracker.Http;

import TaskTracker.Http.Handlers.*;
import TaskTracker.Managers.Managers;
import TaskTracker.Managers.TaskManager;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public final static int PORT = 8080;
    public Gson gson;
    private HttpServer server;
    public TaskManager man;


    public HttpTaskServer() throws IOException {
        man = Managers.getDefault();
        gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks/task/", new TaskHandler(man, gson));
        server.createContext("/tasks/subtask/", new SubtaskHandler(man, gson));
        server.createContext("/tasks/epic/", new EpicHandler(man, gson));
        server.createContext("/tasks/", new PrioritizedTasksHandler(man, gson));
        server.createContext("/tasks/history/", new HistoryHandler(man, gson));
    }

    public void start() {
        System.out.println("Сервер запущен" );
        server.start();
    }
    public void stop() {
        System.out.println("Сервер остановлен" );
        server.stop(1);
    }
}
