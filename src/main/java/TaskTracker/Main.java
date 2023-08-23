package TaskTracker;

import TaskTracker.Http.Handlers.TaskHandler;
import TaskTracker.Http.HttpTaskServer;
import TaskTracker.Http.KVServer;
import TaskTracker.Http.LocalDateTimeTypeAdapter;
import TaskTracker.Managers.History.FileBackedTaskManager;
import TaskTracker.Managers.Managers;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.TaskStatus;
import TaskTracker.Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var KVServ = new KVServer();
        KVServ.start();
        var serv = new HttpTaskServer();
        serv.start();
        Gson gson = serv.gson;
        var man = serv.man;

//        Epic epic1 = new Epic("epic1", "some description",TaskStatus.DONE, null, 0 , null);
//        man.addEpic(epic1);
//        Epic epic2 = new Epic(1,"epic1", "some description",TaskStatus.DONE, null, 0 , null);
//        man.updateEpic(epic2);

    }
}