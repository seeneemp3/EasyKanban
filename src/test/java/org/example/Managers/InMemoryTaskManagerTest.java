package org.example.Managers;

import org.example.Tasks.Epic;
import org.example.Tasks.SubTask;
import org.example.Tasks.Task;
import org.example.Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryTaskManagerTest {
    InMemoryTaskManager man;
    @BeforeEach
    public void setup(){
        man = new InMemoryTaskManager();
    }

    @Test
    void addTask() {
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        assertEquals(1, man.tasks.size());
    }

    @Test
    void getAllTasks() {
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task3", "some description", TaskStatus.NEW));
        assertEquals(3, man.getAllTasks().size());
    }

    @Test
    void deleteAllTasks() {
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW));
        man.deleteAllTasks();
        assertEquals(0, man.tasks.size());
    }

    @Test
    void getTask() {
        Task t = new Task("Task1", "some description", TaskStatus.NEW);
        man.addTask(t);
        assertEquals(t, man.getTask(1));
    }

    @Test
    void deleteTask() {
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        man.deleteTask(1);
        assertEquals(0, man.tasks.size());
    }

    @Test
    void updateTask() {
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW));
        Task t = new Task(1, "Task1", "some description", TaskStatus.DONE);
        man.updateTask(t);
        assertEquals(man.getTask(1), t);
    }

    @Test
    void addEpic() {
        man.addEpic(new Epic("Task1", "some description"));
        assertEquals(1, man.epics.size());
    }

    @Test
    void getAllEpics() {
        man.addEpic(new Epic("Task1", "some description"));
        man.addEpic(new Epic("Task2", "some description"));
        assertEquals(2, man.getAllEpics().size());
    }

    @Test
    void deleteAllEpics() {
        man.addEpic(new Epic("Task1", "some description"));
        man.addEpic(new Epic("Task2", "some description"));
        man.deleteAllEpics();
        assertEquals(0, man.epics.size());
    }

    @Test
    void getEpic() {
        Epic e = new Epic("Task1", "some description");
        man.addEpic(e);
        assertEquals(e, man.getEpic(1));
    }

    @Test
    void deleteEpic() {
        man.addEpic(new Epic("Task1", "some description"));
        man.deleteEpic(1);
        assertEquals(0, man.epics.size());
    }

    @Test
    void updateEpic() {
        man.addEpic(new Epic("111111", "some description"));
        Epic e = new Epic(1, "Epic1", "some description");
        man.updateEpic(e);
        assertEquals(man.getEpic(1), e);
    }

    @Test
    void updateEpicStatus() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        assertEquals(TaskStatus.NEW, e.getStatus() );

        man.addSubTask( new SubTask("Task1", "some description",TaskStatus.NEW, e.getId()));
        assertEquals(TaskStatus.NEW, e.getStatus() );

        man.addSubTask(new SubTask("Task2", "some description",TaskStatus.IN_PROGRESS, e.getId()));
        assertEquals(TaskStatus.IN_PROGRESS, e.getStatus() );

        man.updateSubtask(new SubTask(2,"Task1", "some description",TaskStatus.DONE, e.getId()));
        man.updateSubtask(new SubTask(3,"Tasdasda1", "some description",TaskStatus.DONE, e.getId()));
        assertEquals(TaskStatus.DONE, e.getStatus());

        man.deleteAllSubtasks();
        assertEquals(TaskStatus.NEW, e.getStatus());

    }

    @Test
    void getEpicsSubTasks() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        assertTrue(man.getEpicsSubTasks(e.getId()).isEmpty());

        man.addSubTask( new SubTask("Task1", "some description",TaskStatus.NEW, e.getId()));
        man.addSubTask( new SubTask("Tasqweqe", "some description",TaskStatus.NEW, e.getId()));
        assertEquals(2, man.getEpicsSubTasks(e.getId()).size());
    }

    @Test
    void addSubTask() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1));
        assertEquals(1, man.subtasks.size());

        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,12));
        assertEquals(1, man.subtasks.size());
    }

    @Test
    void getAllSubTasks() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1));
        man.addSubTask(new SubTask("Taadsda", "some description",TaskStatus.NEW,1));
        assertEquals(2, man.getAllSubTasks().size());
    }

    @Test
    void deleteAllSubtasks() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1));
        man.addSubTask(new SubTask("Taadsda", "some description",TaskStatus.NEW,1));
        man.deleteAllSubtasks();
        assertEquals(0, man.getAllSubTasks().size());
    }

    @Test
    void getSubTask() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        SubTask s = new SubTask("Task1", "some description",TaskStatus.NEW,1);
        man.addSubTask(s);
        assertEquals(s, man.getSubTask(2));
    }

    @Test
    void deleteSubTask() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1));
        man.addSubTask(new SubTask("Taadsda", "some description",TaskStatus.NEW,1));
        man.deleteSubTask(2);
        assertEquals(1, man.getAllSubTasks().size());
    }

    @Test
    void updateSubtask() {
        Epic e = new Epic("Epic1", "some description");
        man.addEpic(e);
        SubTask s = new SubTask("Task1", "some description",TaskStatus.NEW,1);
        SubTask s2 = new SubTask(1,"updeasdasda", "some description",TaskStatus.DONE,1);
        man.updateSubtask(s2);
        assertEquals(man.getSubTask(1), s2);
    }
}