package TaskTracker.task;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Epic extends Task {
    protected LocalDateTime endTime;
    Map<Integer, TaskStatus> subtasksIds = new HashMap<>();


    public Epic(int id, String name, String description, TaskStatus status, LocalDateTime time, int duration, LocalDateTime endTime) {
        super(id, name, description, status, time, duration);
        this.type = TaskType.EPIC;
        this.endTime = endTime;
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime time, int duration, LocalDateTime endTime) {
        super(name, description, status, time, duration);
        this.type = TaskType.EPIC;
        this.endTime = endTime;
    }


    public List<Integer> getSubtasksIds() {
        return subtasksIds.keySet().stream().toList();
    }

    public void cleanSubtaskIds() {
        subtasksIds.clear();
    }

    public void removeSubtask(int id) {
        subtasksIds.remove(id);
    }

    public Map<Integer, TaskStatus> getsubtasksIdsMap() {
        return subtasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return getId() + "," + getType() + "," + getName() + "," + getStatus() + "," + getDescription() + "," + getStartTime() + "," + getDuration() + "," + getEndTime();
    }
}
