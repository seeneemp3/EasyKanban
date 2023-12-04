package TaskTracker.manager;

import TaskTracker.http.HTTPTaskManager;
import TaskTracker.http.LocalDateTimeTypeAdapter;
import TaskTracker.manager.history.FileBackedTaskManager;
import TaskTracker.manager.history.HistoryManager;
import TaskTracker.manager.history.InMemoryHistoryManager;
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
