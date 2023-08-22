package TaskTracker.Managers;

import TaskTracker.Http.HTTPTaskManager;
import TaskTracker.Http.HttpTaskServer;
import TaskTracker.Http.KVServer;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.TaskStatus;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTests {

    HttpClient client = HttpClient.newHttpClient();
    private Gson gson;

    KVServer kvServer;
    HttpTaskServer httpTaskServer;

    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        gson = Managers.getGson();
    }

    @Test
    void saveEndpointsByHttpAndCheckRequests() throws IOException, InterruptedException {
        start();
        HTTPTaskManager manager =(HTTPTaskManager)httpTaskServer.man;
        Task task = new Task( "task1", "Desc1", TaskStatus.NEW, LocalDateTime.of(2022, 6, 9, 3, 0),25);
        Epic epic1 = new Epic("epic1", "some description",TaskStatus.DONE, null, 0 , null);
        SubTask subtask1 = new SubTask( "subtask1", "Desc1", TaskStatus.NEW, 2, LocalDateTime.of(2022, 6, 9, 5, 0), 2);
        SubTask subtask2 = new SubTask( "subtask2", "Desc2", TaskStatus.NEW, 2, LocalDateTime.of(2022, 6, 9, 9, 0), 2);

        URI epicUri = URI.create("http://localhost:8080/tasks/epic/");
        URI subtasksUri = URI.create("http://localhost:8080/tasks/subtask/");
        URI taskUri = URI.create("http://localhost:8080/tasks/task/");

        manager.addTask(task);
        manager.addEpic(epic1);
        manager.addSubTask(subtask1);
        manager.addSubTask(subtask2);

        String taskJson = gson.toJson(task);
        String epicJson1 = gson.toJson(epic1);
        String subtaskJson1 = gson.toJson(subtask1);
        String subtaskJson2 = gson.toJson(subtask2);

        System.out.println(taskJson);
         //Thread.sleep(20000);

//        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(taskJson);
//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/task/" + "?id=1")).GET().build();
//        assertEquals(taskJson, client.send(request, HttpResponse.BodyHandlers.ofString()).body());

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
    }
}
