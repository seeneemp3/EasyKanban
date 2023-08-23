package TaskTracker.Managers.History;

import TaskTracker.Tasks.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void addTask(Task task);
    void remove(int id);
//    String toString();
}