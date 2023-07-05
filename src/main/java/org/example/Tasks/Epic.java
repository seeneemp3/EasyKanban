package org.example.Tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Epic extends Task{
    Map<Integer, TaskStatus> subtasksIds = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description, null);
        this.type = TaskType.EPIC;
    }
    public Epic(int id, String name, String description) {
        super(name, description, null);
        this.id = id;
        this.type = TaskType.EPIC;
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds.keySet().stream().toList();
    }

//    public void setSubtasksIds(List<Integer> subtasksIds) {
//        this.subtasksIds = subtasksIds;
//    }
    public void cleanSubtaskIds(){
        subtasksIds.clear();
    }
    public void removeSubtask(int id){
        subtasksIds.remove(id);
    }
    public Map<Integer, TaskStatus> getsubtasksIdsMap(){
        return subtasksIds;
    }









    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIds=" + subtasksIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
