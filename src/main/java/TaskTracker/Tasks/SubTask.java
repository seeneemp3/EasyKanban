package TaskTracker.Tasks;

import java.time.LocalDateTime;

public class SubTask extends Task{
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, int duration) {
        super(id, name, description, status, startTime, duration);
        this.id = id;
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return this.epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + ","  + getType()+ "," + getName() + "," + getStatus() + "," + getDescription() + ","  + getId() + ","  + getStartTime() + "," + getDuration() ;
    }
}
