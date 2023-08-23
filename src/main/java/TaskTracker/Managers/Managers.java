package TaskTracker.Managers;

import TaskTracker.Http.HTTPTaskManager;
import TaskTracker.Http.LocalDateTimeTypeAdapter;
import TaskTracker.Managers.History.FileBackedTaskManager;
import TaskTracker.Managers.History.HistoryManager;
import TaskTracker.Managers.History.InMemoryHistoryManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDateTime;

public class Managers {

    public static HistoryManager getDefaultHistory(){
        return  new InMemoryHistoryManager();
    }
    public static FileBackedTaskManager getDefaultFile(){
        return new FileBackedTaskManager(new File("src/data/task.csv"));
    }
    public static TaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078/");
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        return gsonBuilder.create();
    }
}
