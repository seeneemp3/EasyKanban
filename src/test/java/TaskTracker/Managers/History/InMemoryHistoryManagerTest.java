package TaskTracker.Managers.History;

import TaskTracker.Managers.InMemoryTaskManager;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.SubTask;
import TaskTracker.Tasks.Task;
import TaskTracker.Tasks.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager man;
    HistoryManager him;
    @BeforeEach
    public void setup(){
        man = new InMemoryTaskManager();
        him = man.historyManager;
    }

    @Test
    void getHistory() {
        assertEquals(0, him.getHistory().size(), "История не пустая");
        man.addTask(new Task("Task1", "some description", TaskStatus.NEW, LocalDateTime.now(),33));
        man.addTask(new Task("Task2", "some description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(60),32));
        man.addTask(new Task("Task3", "some description", TaskStatus.DONE, LocalDateTime.now().plusMinutes(120),13));
        man.getTask(1);
        man.getTask(1);
        man.getTask(2);
        assertEquals(2, him.getHistory().size(),"Неверное количество таксок в истории");
    }

    @Test
    void addTask() {
        Task t = new Task("Task4", "some description", TaskStatus.NEW, LocalDateTime.now(),13);
        man.addTask(t);
        man.getTask(t.getId());
        assertEquals(1, him.getHistory().size(), "Таска не была добавлена или добавлена неправильно");
    }

    @Test
    void remove() {
        man.addTask(new Task("Task5", "some description", TaskStatus.NEW, LocalDateTime.now(),32));
        man.addTask(new Task("Task5", "some description", TaskStatus.NEW, LocalDateTime.now().plusMinutes(60),222));
        man.addEpic(new Epic("Task7", "some description", null, null, 0 , null));
        man.addEpic(new Epic("Task71", "some descriptionn", null, null, 0 , null));
        man.addSubTask(new SubTask("Task8", "some description", TaskStatus.NEW, 3, LocalDateTime.now().minusMinutes(120),23));
        man.addSubTask(new SubTask("Task81", "some description", TaskStatus.NEW, 3, LocalDateTime.now().minusMinutes(240),13));
        man.getTask(1);
        man.getTask(2);
        man.getEpic(3);
        man.getEpic(4);
        man.getSubTask(5);
        man.getSubTask(6);
        him.remove(1);
        assertEquals(5, him.getHistory().size(), "Таски не удалились");

        man.deleteAllSubtasks();
        assertEquals(3,him.getHistory().size(), "Сабтаски не удалились");

        man.deleteAllEpics();
        assertEquals(1,him.getHistory().size(), "Эпики не удалились");

        man.deleteAllTasks();
        assertEquals(0,him.getHistory().size(), "Таски не удалились");
    }
}