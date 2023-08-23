package TaskTracker.Managers;

import TaskTracker.Http.HTTPTaskManager;
import TaskTracker.Http.HttpTaskServer;
import TaskTracker.Http.KVServer;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.TaskStatus;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTests {

    private Task task;
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;

    private final URI epicUri = URI.create("http://localhost:8080/tasks/epic/");
    private final URI subtasksUri = URI.create("http://localhost:8080/tasks/subtask/");
    private final URI taskUri = URI.create("http://localhost:8080/tasks/task/");


    private final HttpClient client = HttpClient.newHttpClient();
    HTTPTaskManager man;
    public Gson gson;


    private  String taskJson;
    private  String epicJson1;
    private  String subtaskJson1;
    private  String subtaskJson2;

    KVServer kvServer;
    HttpTaskServer httpTaskServer;
    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        gson = Managers.getGson();
        man = (HTTPTaskManager) httpTaskServer.man;

        init();
    }


    void init(){
         task = new Task( "task1", "Desc1", TaskStatus.NEW, LocalDateTime.of(2022, 6, 9, 3, 0),25);
         epic = new Epic("epic1", "some description",TaskStatus.DONE, null, 0 , null);
         subTask1 = new SubTask( "subtask1", "Desc1", TaskStatus.NEW, 2, LocalDateTime.of(2022, 6, 9, 5, 0), 2);
         subTask2 = new SubTask( "subtask2", "Desc2", TaskStatus.NEW, 2, LocalDateTime.of(2022, 6, 9, 9, 0), 2);
         taskJson = gson.toJson(task);
         epicJson1 = gson.toJson(epic);
         subtaskJson1 = gson.toJson(subTask1);
         subtaskJson2 = gson.toJson(subTask2);
    }

    @Test
    void bigTestIsBad() throws IOException, InterruptedException {

        // status code 200 on POST request test

        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
        HttpRequest request = HttpRequest.newBuilder().uri(taskUri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodyEpic1 = HttpRequest.BodyPublishers.ofString(epicJson1);
        HttpRequest requestEpic1 = HttpRequest.newBuilder().uri(epicUri).POST(bodyEpic1).build();
        HttpResponse<String> responseEpic1 = client.send(requestEpic1, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodySubtask1 = HttpRequest.BodyPublishers.ofString(subtaskJson1);
        HttpRequest requestSubtask1 = HttpRequest.newBuilder().uri(subtasksUri).POST(bodySubtask1).build();
        HttpResponse<String> responseSubtask1 = client.send(requestSubtask1, HttpResponse.BodyHandlers.ofString());

        final HttpRequest.BodyPublisher bodySubtask2 = HttpRequest.BodyPublishers.ofString(subtaskJson2);
        HttpRequest requestSubtask2 = HttpRequest.newBuilder().uri(subtasksUri).POST(bodySubtask2).build();
        HttpResponse<String> responseSubtask2 = client.send(requestSubtask2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(200, responseEpic1.statusCode());
        assertEquals(200, responseSubtask1.statusCode());
        assertEquals(200, responseSubtask2.statusCode());

        // GET should return correct json task

        taskJson = gson.toJson(man.getTask(1));
        epicJson1 = gson.toJson(man.getEpic(2));
        subtaskJson1 = gson.toJson(man.getSubTask(3));
        subtaskJson2 = gson.toJson(man.getSubTask(4));

        HttpRequest requestTask = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task/" + "?id=1")).GET().build();
        HttpRequest requestEpic = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/epic/" + "?id=2")).GET().build();
        HttpRequest requestSub1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask/" + "?id=3")).GET().build();
        HttpRequest requestSub2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/subtask/" + "?id=4")).GET().build();

        assertEquals(taskJson, client.send(requestTask, HttpResponse.BodyHandlers.ofString()).body());
        assertEquals(epicJson1, client.send(requestEpic, HttpResponse.BodyHandlers.ofString()).body());
        assertEquals(subtaskJson1, client.send(requestSub1, HttpResponse.BodyHandlers.ofString()).body());
        assertEquals(subtaskJson2, client.send(requestSub2, HttpResponse.BodyHandlers.ofString()).body());


    }
}
