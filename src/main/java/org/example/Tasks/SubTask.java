package org.example.Tasks;

public class SubTask extends Task{
    protected int epicId;

    public SubTask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }
    public SubTask(int id, String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.id = id;
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
