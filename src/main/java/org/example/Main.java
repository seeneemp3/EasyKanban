package org.example;

import org.example.Managers.InMemoryTaskManager;
import org.example.Tasks.Task;
import org.example.Tasks.TaskStatus;

public class Main {
    public static void main(String[] args) {
        var man = new InMemoryTaskManager();
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task2", "some description", TaskStatus.DONE));
        man.addTask(new Task("Task3", "some description", TaskStatus.IN_PROGRESS));




    }
}