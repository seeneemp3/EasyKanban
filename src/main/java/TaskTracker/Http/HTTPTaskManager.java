package TaskTracker.Http;

import TaskTracker.Managers.History.FileBackedTaskManager;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static TaskTracker.Managers.Managers.getGson;

public class HTTPTaskManager extends FileBackedTaskManager {
    private final KVTaskClient client;
    private final Gson gson = getGson();

    public HTTPTaskManager(String url) {
        this.client = new KVTaskClient(url);
    }

    @Override
    public void save() {
        try {
            String jsonTask = gson.toJson(tasks);
            client.put("TASK", jsonTask);
            String jsonEpic = gson.toJson(epics);
            client.put("EPIC", jsonEpic);
            String jsonSub = gson.toJson(subtasks);
            client.put("SUBTASK", jsonSub);
            List<Task> history = getHistory();
            String jsonHistory = gson.toJson(history.stream().map(Task::getId).collect(Collectors.toList()));
            client.put("HISTORY", jsonHistory);
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred.");
        }
    }

    public void load() {
        try {
            String jsonTask = client.load("TASK");
            final HashMap<Integer, Task> restoredTasks = gson.fromJson(jsonTask, new TypeToken<HashMap<Integer, Task>>() {
            }.getType());
            tasks.putAll(restoredTasks);

            String jsonEpic = client.load("EPIC");
            final HashMap<Integer, Epic> restoredEpics = gson.fromJson(jsonEpic, new TypeToken<HashMap<Integer, Epic>>() {
            }.getType());
            epics.putAll(restoredEpics);

            String jsonSub = client.load("SUBTASK");
            final HashMap<Integer, SubTask> restoredSubs = gson.fromJson(jsonSub, new TypeToken<HashMap<Integer, SubTask>>() {
            }.getType());
            subtasks.putAll(restoredSubs);

            String jsonHistory = client.load("HISTORY");
            final List<Integer> historyIds = gson.fromJson(jsonHistory, new TypeToken<List<Integer>>() {
            }.getType());

            HashMap<Integer, Task> allTasks = new HashMap<>(tasks);
            allTasks.putAll(epics);
            allTasks.putAll(subtasks);
            historyIds.forEach(s -> historyManager.addTask(allTasks.get(s)));
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred.");
        }
    }

}
