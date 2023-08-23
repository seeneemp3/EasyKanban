package TaskTracker.Managers;

import TaskTracker.Managers.History.HistoryManager;
import TaskTracker.TaskExeptions.InsertTaskExeption;
import TaskTracker.Tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager{
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, SubTask> subtasks = new HashMap<>();
    public int globalId = 0;
    public final HistoryManager historyManager = Managers.getDefaultHistory();
    private Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));


/////////////////////////////////////////////////////////
/////////////////////TASK////////////////////////////////
/////////////////////////////////////////////////////////
    public void addTask(Task task) {
        int id = ++globalId;
        task.setId(id);
        tasks.put(id, task);
        checkTime(task);
        prioritizedTasks.add(task);
    }
    public List<Task> getAllTasks(){
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Integer id : tasks.keySet()) {
            allTasks.add(tasks.get(id));
        }
        return allTasks;
    }
    public void deleteAllTasks(){
    for (Task t : tasks.values()){
        if (historyManager.getHistory().contains(t)) historyManager.remove(t.getId());
        prioritizedTasks.remove(t);
    }
    tasks.clear();
    }
    public Task getTask(int id) {
        if (!tasks.containsKey(id)) throw new IllegalArgumentException("Таски с таким ид нет");
        Task t = tasks.get(id);
        historyManager.addTask(tasks.get(id));
        return t;
    }
    public void deleteTask(int id){
        if (!tasks.containsKey(id)) throw new IllegalArgumentException("Таски с таким ид нет");
        if (historyManager.getHistory().contains(tasks.get(id))) historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);

    }
    public void updateTask(Task task){
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        checkTime(task);
        prioritizedTasks.add(task);
    }

/////////////////////////////////////////////////////////
/////////////////////EPIC////////////////////////////////
/////////////////////////////////////////////////////////
    public void addEpic(Epic epic){
        int id = ++globalId;
        epic.setId(id);
        epics.put(id, epic);
        updateEpicStatus(epic.getId());
        updateEpicTime(epic);
        checkTime(epic);
        prioritizedTasks.add(epic);
    }
    public List<Epic> getAllEpics(){
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic: epics.values()) {
            updateEpicStatus(epic.getId());
            updateEpicTime(epic);
        }
        for (Integer id : epics.keySet()) {
            allEpics.add(epics.get(id));
        }
        return allEpics;
    }
    public void deleteAllEpics(){
        for (Epic e : epics.values()){
            if (historyManager.getHistory().contains(e)) historyManager.remove(e.getId());
            prioritizedTasks.remove(e);
        }
        epics.clear();
        deleteAllSubtasks();
    }
    public Epic getEpic(int id){
        if (!epics.containsKey(id)) throw new IllegalArgumentException("Эпика с таким ид нет");
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }
    public void deleteEpic (int id) {
        if (!epics.containsKey(id)) throw new IllegalArgumentException("Эпика с таким ид нет");
        if (historyManager.getHistory().contains(epics.get(id))) historyManager.remove(id);                                 //history
        Epic epic = epics.get(id);
        List <Integer> doubleSubs = List.copyOf(epic.getSubtasksIds());
        for(Integer subId : doubleSubs){
            prioritizedTasks.remove(subtasks.get(subId));
            deleteSubTask(subId);
        }
        prioritizedTasks.remove(epics.get(id));
        epics.remove(id);
    }

    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        prioritizedTasks.remove(epics.get(epic.getId()));
        ///////update epic status/////////
        updateEpicStatus(epic.getId());
        updateEpicTime(epic);
        if (epic.getStartTime() != null) checkTime(epic);
        prioritizedTasks.add(epic);
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

    @Override
    public void updateEpicTime(Epic epic) {
        List <Integer> subs = epic.getSubtasksIds();
        LocalDateTime startTime = LocalDateTime.MIN;
        LocalDateTime endTime = LocalDateTime.MAX;
        if (!subs.isEmpty()){
            startTime = subtasks.get(subs.get(0)).getStartTime();
            endTime = subtasks.get(subs.get(0)).getStartTime();
                for (int i = 1; i < subs.size(); i++) {
                    LocalDateTime time = subtasks.get(subs.get(i)).getStartTime();
                    if (startTime.isAfter(time)) {
                        startTime = time;
                    }
                    if (endTime.isBefore(time)) {
                        endTime = time;
                    }
                }
                epic.setStartTime(startTime);
                epic.setEndTime(endTime);
                updateEpicDuration(epic);
        }
    }

    @Override
    public int updateEpicDuration(Epic epic) {
        List <Integer> subs = epic.getSubtasksIds();
        int duration = 0;
        for (Integer sub : subs) {
            duration = duration + subtasks.get(sub).getDuration();
        }
        epic.setDuration(duration);
        return duration;
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
            updateEpicTime(epic);
            checkTime(subTask);
            prioritizedTasks.add(subTask);
        }else throw new IllegalArgumentException("Не возможно создать подзадачу без эпика");
    }
    public List<SubTask> getAllSubTasks(){
        return subtasks.values().stream().toList();
    }
    public void deleteAllSubtasks(){
        epics.values().forEach(Epic::cleanSubtaskIds);
        for (SubTask s : subtasks.values()){
            if (historyManager.getHistory().contains(s)) historyManager.remove(s.getId());
            prioritizedTasks.remove(s);
        }
        subtasks.clear();
        ///////update epic status/////////
        epics.values().forEach(s -> {
            updateEpicStatus(s.getId());
            updateEpicTime(s);
        });
    }
    public SubTask getSubTask(int id){
       if (subtasks.containsKey(id)) {
           historyManager.addTask(subtasks.get(id));
       } else throw new IllegalArgumentException("Подзадачи с таким ид нет");
       return subtasks.get(id);
    }
    public void deleteSubTask(int id){
        if(!subtasks.containsKey(id)) throw new IllegalArgumentException("Подзадачи с таким ид нет");
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        epic.removeSubtask(id);
         if (historyManager.getHistory().contains(subtasks.get(id))) historyManager.remove(id);
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        //////update epic status//////
        updateEpicStatus(epic.getId());
        updateEpicTime(epic);
    }
    public void updateSubtask(SubTask subTask){
        SubTask old = subtasks.get(subTask.getId());
        prioritizedTasks.remove(old);
        subtasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic != null) {
            epic.getsubtasksIdsMap().put(subTask.getId(),subTask.getStatus());
            //////update epic status//////
            updateEpicStatus(epic.getId());
            updateEpicTime(epic);
            checkTime(subTask);
            prioritizedTasks.add(subTask);
        }

    }

/////////////////////////////////////////////////////////
/////////////////////OTHER///////////////////////////////
/////////////////////////////////////////////////////////

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void checkTime(Task task){
        if (task.getStartTime() != null) {
            LocalDateTime startTime = task.getStartTime();
            LocalDateTime endTime = task.getEndTime();
            if (!prioritizedTasks.isEmpty()) {
                for (Task taskTree : prioritizedTasks) {
                        if ((!taskTree.equals(task)) &&
                                 (!( taskTree.getType() == TaskType.EPIC && ((Epic)taskTree).getSubtasksIds().contains(task.getId()))) &&
                                !(startTime.isBefore(taskTree.getStartTime()) && endTime.isBefore(taskTree.getStartTime()) || startTime.isAfter(taskTree.getEndTime()) && endTime.isAfter(taskTree.getEndTime()))) {
                            throw new InsertTaskExeption("Задачи нельзя выполнять одновременно\n" + "задача " + taskTree.getName() + " пересекается с " + task.getName());
                        }
                }
            }
        }

    }
}
