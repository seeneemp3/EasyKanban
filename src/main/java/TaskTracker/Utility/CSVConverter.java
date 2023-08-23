package TaskTracker.Utility;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import TaskTracker.Managers.History.InMemoryHistoryManager;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.TaskStatus;
import TaskTracker.Tasks.TaskType;

public class CSVConverter {
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

}
