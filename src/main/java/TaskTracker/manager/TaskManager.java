package TaskTracker.manager;

import TaskTracker.task.Epic;
import TaskTracker.task.SubTask;
import TaskTracker.task.Task;

import java.util.List;
import java.util.Set;

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
    public void updateEpicTime(Epic epic) ;
    public int updateEpicDuration(Epic epic);
    Set <Task> getPrioritizedTasks();
    List<Task> getHistory();
}
