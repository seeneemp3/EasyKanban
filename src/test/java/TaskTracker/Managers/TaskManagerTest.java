package TaskTracker.Managers;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager>{

    protected T man;
    protected T emptyManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask1;
    protected SubTask subtask2;

    protected void initTasks() {
        task = new Task("Task1", "some description", TaskStatus.NEW, LocalDateTime.now(), 25);
        epic = new Epic("epic1", "some description",null, null, 0 , null );
        subtask1 = new SubTask("subTask1", "some description",TaskStatus.NEW, 1, LocalDateTime.now().plusMinutes(100), 25);
        subtask2 = new SubTask("subTask2", "some description",TaskStatus.NEW, 1, LocalDateTime.now().plusMinutes(200), 40);
    }


    @Test
    void addTask() {
        man.addTask(task);
        assertEquals(1, man.getAllTasks().size());
    }

    @Test
    void getAllTasks() {
        man.addTask(task);
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(100),13));
        man.addTask(new Task("Task3", "some description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(200),13));
        assertEquals(3, man.getAllTasks().size());
    }

    @Test
    void deleteAllTasks() {
        man.addTask(task);
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(100),13));
        man.deleteAllTasks();
        assertEquals(0, man.getAllTasks().size());
    }

    @Test
    void getTask() {
        man.addTask(task);
        assertEquals(task, man.getTask(1));
    }

    @Test
    void deleteTask() {
        man.addTask(task);
        man.deleteTask(1);
        assertEquals(0, man.getAllTasks().size());
    }

    @Test
    void updateTask() {
        man.addTask(task);
        Task t = new Task(1, "Task1", "some description", null, LocalDateTime.now(),13);
        man.updateTask(t);
        assertEquals(man.getTask(1), t);
    }

    @Test
    void addEpic() {
        //man.addSubTask(subtask);
        man.addEpic(epic);

        final List<Epic> epics = man.getAllEpics();
        assertEquals(1, epics.size());
    }

    @Test
    void getAllEpics() {
        man.addEpic(epic);
        man.addEpic(new Epic("Task2", "some description",null, null, 0 , null));
        assertEquals(2, man.getAllEpics().size());
    }

    @Test
    void deleteAllEpics() {
        man.addEpic(epic);
        man.addEpic(new Epic("Task2", "some description",null, null, 0 , null));
        man.deleteAllEpics();
        assertEquals(0, man.getAllEpics().size());
    }

    @Test
    void getEpic() {
        man.addEpic(epic);
        assertEquals(epic, man.getEpic(1));
    }

    @Test
    void deleteEpic() {
        man.addEpic(epic);
        man.addSubTask(new SubTask("s","des",TaskStatus.NEW,1, LocalDateTime.now().plusMinutes(300),13));
        List<Integer> subs = epic.getSubtasksIds();
        man.deleteEpic(1);
        assertEquals(0, man.getAllEpics().size());

        assertThrows(IllegalArgumentException.class, () -> man.getSubTask(2));
    }

    @Test
    void updateEpic() {
        man.addEpic(epic);
        Epic e = new Epic(1, "Epic11111", "some description",null, null, 0 , null);
        man.updateEpic(e);
        assertEquals(man.getEpic(1), e);
    }

    @Test
    void updateEpicStatus() {
        man.addEpic(epic);
        assertEquals(TaskStatus.NEW, epic.getStatus() );

        man.addSubTask( new SubTask("Task1", "some description",TaskStatus.NEW, epic.getId(), LocalDateTime.now().minusMinutes(100),13));
        assertEquals(TaskStatus.NEW, epic.getStatus() );

        man.addSubTask(new SubTask("Task2", "some description",TaskStatus.IN_PROGRESS, epic.getId(), LocalDateTime.now().plusMinutes(100),13));
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus() );

        man.updateSubtask(new SubTask(2,"Task1", "some description",TaskStatus.DONE, epic.getId(), LocalDateTime.now().minusMinutes(100),13));
        man.updateSubtask(new SubTask(3,"Tasdasda1", "some description",TaskStatus.DONE, epic.getId(), LocalDateTime.now().plusMinutes(100),13));
        assertEquals(TaskStatus.DONE, epic.getStatus());

        man.deleteAllSubtasks();
        assertEquals(TaskStatus.NEW, epic.getStatus());

    }

    @Test
    void getEpicsSubTasks() {
        man.addEpic(epic);
        assertTrue(man.getEpicsSubTasks(epic.getId()).isEmpty());
        man.addSubTask(new SubTask("sub1", "desc", TaskStatus.NEW, epic.getId(), LocalDateTime.now(),13));

        assertEquals(1, man.getEpicsSubTasks(epic.getId()).size());
    }

    @Test
    void addSubTask() {
        man.addEpic(epic);
        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1, LocalDateTime.now().plusMinutes(100),13));
        assertEquals(1, man.getAllSubTasks().size());

        man.addSubTask(new SubTask("Task1", "some description",TaskStatus.NEW,1, LocalDateTime.now().minusMinutes(100),13));
        assertEquals(2, man.getAllSubTasks().size());
    }

    @Test
    void getAllSubTasks() {
        man.addEpic(epic);
        man.addSubTask(subtask1);
        man.addSubTask(subtask2);
        assertEquals(2, man.getAllSubTasks().size());
    }

    @Test
    void deleteAllSubtasks() {
        man.addEpic(epic);
        man.addSubTask(subtask1);
        man.addSubTask(subtask2);
        man.deleteAllSubtasks();
        assertEquals(0, man.getAllSubTasks().size());
    }

    @Test
    void getSubTask() {
        man.addEpic(epic);
        SubTask s = new SubTask("Task1", "some description",TaskStatus.NEW,1, LocalDateTime.now(),13);
        man.addSubTask(s);
        assertEquals(s, man.getSubTask(2));
    }

    @Test
    void deleteSubTask() {
        man.addEpic(epic);
        man.addSubTask(subtask1);
        man.addSubTask(subtask2);
        man.deleteSubTask(2);
        assertEquals(1, man.getAllSubTasks().size());
    }

    @Test
    void updateSubtask() {
        man.addEpic(epic);
        man.addSubTask(subtask1);
        SubTask s = new SubTask(2,"subTask2", "some description",TaskStatus.NEW, 1, LocalDateTime.now(), 40);
        man.updateSubtask(s);
        assertEquals(man.getSubTask(2), s);
    }
}
