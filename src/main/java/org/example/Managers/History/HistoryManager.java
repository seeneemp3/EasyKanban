package org.example.Managers.History;

import org.example.Tasks.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void addTask(Task task);
    void remove(int id);
}