package org.example.Managers;

import org.example.Tasks.Epic;
import org.example.Tasks.SubTask;
import org.example.Tasks.Task;
import org.example.Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    public void addTask(Task task);
    public List<Task> getAllTasks();
    public void deleteAllTasks();
    public Task getTask(int id);
    public void deleteTask(int id);
    public void updateTask(Task task);
    public void addEpic(Epic epic);
    public List<Epic> getAllEpics();
    public void deleteAllEpics();
    public Epic getEpic(int id);
    public void deleteEpic (int id);
    public void updateEpic(Epic epic);
    public void updateEpicStatus(int epicId);
    public List<SubTask> getEpicsSubTasks(int epicId);
    public void addSubTask(SubTask subTask);
    public List<SubTask> getAllSubTasks();
    public void deleteAllSubtasks();
    public SubTask getSubTask(int id);
    public void deleteSubTask(int id);
    public void updateSubtask(SubTask subTask);
}
