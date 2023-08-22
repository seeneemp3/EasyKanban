package TaskTracker.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.TaskStatus;
import TaskTracker.Tasks.TaskType;

public class CSVConverter {
    public static String toString(Task task){
        String taskToString = "";
        if (task.getType().equals(TaskType.SUBTASK)){
            SubTask subTask = (SubTask) task;
            taskToString = subTask.getId() + ","  + subTask.getType()+ "," + subTask.getName() + "," + subTask.getStatus() + "," + subTask.getDescription() + ","  + subTask.getId() + ","  + subTask.getStartTime() + "," + subTask.getDuration() ;
        } else if(task.getType().equals(TaskType.EPIC)){
            Epic epic = (Epic) task;
            taskToString = epic.getId() + "," + epic.getType() + "," + epic.getName() + "," + epic.getStatus() + "," + epic.getDescription()   + "," + epic.getStartTime() + "," + epic.getDuration() + "," + epic.getEndTime();
        } else{
            taskToString = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription()  + ","  + task.getStartTime() + "," + task.getDuration();
        }
        return taskToString;
    }

    public static Task toTask(String str){
        String [] values = str.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[5]);;
        int duration = Integer.parseInt(values[6]);

        if (type.equals(TaskType.TASK)){
            return new Task(id, name, description, status, startTime, duration);
        }else if (type.equals(TaskType.EPIC)){
            LocalDateTime endTime = LocalDateTime.parse(values[7]);
            return new Epic (id, name, description, status, startTime, duration, endTime);
        }else {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(id, name, description, status, epicId, startTime, duration);
        }
    }

    public static String historyToString(List<Task> taskHistory){
        return taskHistory.stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
