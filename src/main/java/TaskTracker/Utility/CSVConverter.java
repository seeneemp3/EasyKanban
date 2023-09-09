package TaskTracker.Utility;

import TaskTracker.Tasks.*;

import java.time.LocalDateTime;

public class CSVConverter {
    public static Task toTask(String str) {
        String[] values = str.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        TaskStatus status = TaskStatus.valueOf(values[3]);
        String description = values[4];
        LocalDateTime startTime = LocalDateTime.parse(values[5]);
        int duration = Integer.parseInt(values[6]);

        if (type.equals(TaskType.TASK)) {
            return new Task(id, name, description, status, startTime, duration);
        } else if (type.equals(TaskType.EPIC)) {
            LocalDateTime endTime = LocalDateTime.parse(values[7]);
            return new Epic(id, name, description, status, startTime, duration, endTime);
        } else {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(id, name, description, status, epicId, startTime, duration);
        }
    }

}
