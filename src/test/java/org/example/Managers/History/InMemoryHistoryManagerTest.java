package org.example.Managers.History;

import org.example.Managers.InMemoryTaskManager;
import org.example.Managers.Managers;
import org.example.Tasks.Task;
import org.example.Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager man;
    HistoryManager him;
    @BeforeEach
    public void setup(){
        man = new InMemoryTaskManager();
        him = new InMemoryHistoryManager();
    }

    @Test
    void getHistory() {
        System.out.println("getHistory() "+ him.getHistory());
        //assertEquals(0, him.getHistory().size());
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task3", "some description", TaskStatus.DONE));
        man.getTask(1);
        man.getTask(2);
        assertEquals(2, him.getHistory().size());
    }

    @Test
    void addTask() {
        System.out.println("addTask() "+ him.getHistory());
        Task t = new Task("Task4", "some description", TaskStatus.NEW);
        man.addTask(t);
        him.addTask(t);
        assertEquals(1, him.getHistory().size() );
    }

    @Test
    void remove() {
        System.out.println("remove() "+ him.getHistory());
        man.addTask(new Task("Task5", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task6", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task7", "some description", TaskStatus.NEW));
        man.getTask(1);
        man.getTask(2);
        man.getTask(3);
        him.remove(1);
        assertEquals(1, him.getHistory().size());
    }
}