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


//        {"id":0,"name":"Task12","description":"some description","status":"IN_PROGRESS","type":"TASK","startTime":"04.08.2023 18.25","duration":10}
//        {"id":0,"name":"Task34","description":"some description","status":"IN_PROGRESS","type":"TASK","startTime":"04.08.2023 17.25","duration":10}
//        {"epicId":1,"id":0,"name":"sub2","description":"desc","status":"DONE","type":"SUBTASK","startTime":"05.08.2023 16.56","duration":10}
        //{"subtasksIds":{},"id":0,"name":"epic1","description":"desc","type":"EPIC","duration":0}




        var KVServ = new KVServer();
        KVServ.start();
        var serv = new HttpTaskServer();
        serv.start();
        Gson gson = serv.gson;

        var man = serv.man;


        Epic epic1 = new Epic("epic1", "some description",TaskStatus.DONE, null, 0 , null);
        man.addEpic(epic1);
        Epic epic2 = new Epic(1,"epic1", "some description",TaskStatus.DONE, null, 0 , null);
        man.updateEpic(epic2);
//        HttpRequest.BodyPublisher bodyEpic1 = HttpRequest.BodyPublishers.ofString(je);
//        HttpRequest requestEpic1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic/")).POST(bodyEpic1).build();
//        HttpClient client = HttpClient.newHttpClient();
//        HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());
//        System.out.println(responseEpic1.statusCode());
//        System.out.println(responseEpic1.body());


//        man.addEpic(new Epic("epic1", "desc", null, null, 0 , null));
//        man.addSubTask(new SubTask("sub1", "desc", TaskStatus.DONE, 1, LocalDateTime.now(), 10));
//        man.addSubTask(new SubTask("sub2", "desc", TaskStatus.DONE, 1, LocalDateTime.now().minusMinutes(30), 10));
//        man.addTask(new Task("Task12", "some description", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(60), 10));
//        man.addTask(new Task("Task34", "some description", TaskStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(75), 10));


    }
}