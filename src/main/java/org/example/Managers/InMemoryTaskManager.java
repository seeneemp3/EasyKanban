package org.example.Managers;

import org.example.Managers.History.HistoryManager;
import org.example.Managers.History.InMemoryHistoryManager;
import org.example.Tasks.Epic;
import org.example.Tasks.SubTask;
import org.example.Tasks.Task;
import org.example.Tasks.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subtasks = new HashMap<>();
    public int globalId = 0;
    public final HistoryManager historyManager = Managers.getDefaultHistory();


/////////////////////////////////////////////////////////
/////////////////////TASK////////////////////////////////
/////////////////////////////////////////////////////////
    public void addTask(Task task) {
        int id = ++globalId;
        task.setId(id);
        tasks.put(id, task);
    }
    public List<Task> getAllTasks(){
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            allTasks.add(tasks.get(id));
        }
        return allTasks;
    }
    public void deleteAllTasks(){
        tasks.clear();
    }
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }
    public void deleteTask(int id){
        tasks.remove(id);
    }
    public void updateTask(Task task){
        tasks.put(task.getId(), task);
    }

/////////////////////////////////////////////////////////
/////////////////////EPIC////////////////////////////////
/////////////////////////////////////////////////////////
    public void addEpic(Epic epic){
        int id = ++globalId;
        epic.setId(id);
        epics.put(id, epic);
        updateEpicStatus(epic.getId());
    }
    public List<Epic> getAllEpics(){
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Integer id : epics.keySet()) {
            allEpics.add(epics.get(id));
        }
        return allEpics;
    }
    public void deleteAllEpics(){
        epics.clear();
    }
    public Epic getEpic(int id){
       return epics.get(id);
    }
    public void deleteEpic (int id) {
        epics.remove(id);
    }

    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        ///////update epic status/////////
        updateEpicStatus(epic.getId());
    }
    public void updateEpicStatus(int epicId){
        Epic epic = epics.get(epicId);
       if (epic.getsubtasksIdsMap().containsValue(TaskStatus.IN_PROGRESS)){
           epic.setStatus(TaskStatus.IN_PROGRESS);
       }else if (!(epic.getsubtasksIdsMap().isEmpty()) && (epic.getsubtasksIdsMap().values().stream().allMatch(s -> s == TaskStatus.DONE))){
           epic.setStatus(TaskStatus.DONE);
       }else epic.setStatus(TaskStatus.NEW);
    }


    public List<SubTask> getEpicsSubTasks(int epicId){
        Epic epic = epics.get(epicId);
        return epic.getSubtasksIds().stream().map(subtasks::get).toList();
    }
/////////////////////////////////////////////////////////
/////////////////////SUBTASK/////////////////////////////
/////////////////////////////////////////////////////////
    public void addSubTask(SubTask subTask){
        int subtaskId = ++globalId;
        Epic epic = epics.get(subTask.getEpicId());

        if (epic != null) {
            subTask.setId(subtaskId);
            subtasks.put(subTask.getId(), subTask);
            epic.getsubtasksIdsMap().put(subTask.getId(),subTask.getStatus() );
            ///////update epic status/////////
            updateEpicStatus(epic.getId());
        }
    }
    public List<SubTask> getAllSubTasks(){
        return subtasks.values().stream().toList();
    }
    public void deleteAllSubtasks(){
        epics.values().forEach(Epic::cleanSubtaskIds);
        subtasks.clear();
        ///////update epic status/////////
        epics.values().forEach(s -> updateEpicStatus(s.getId()));
    }
    public SubTask getSubTask(int id){
       return subtasks.get(id);
    }
    public void deleteSubTask(int id){
      Epic epic = epics.get(subtasks.get(id).getEpicId());
      epic.removeSubtask(id);
      subtasks.remove(id);
      //////update epic status//////
      updateEpicStatus(epic.getId());
    }
    public void updateSubtask(SubTask subTask){
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.getsubtasksIdsMap().put(subTask.getId(),subTask.getStatus());
            //////update epic status//////
            updateEpicStatus(epic.getId());
        }

    }








}
