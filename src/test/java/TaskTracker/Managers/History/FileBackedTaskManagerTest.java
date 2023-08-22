package TaskTracker.Managers.History;

import TaskTracker.Managers.TaskManagerTest;
import TaskTracker.Tasks.Epic;
import TaskTracker.Tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    public void setUp() {
        file = new File("src/test/java/data.csv");
        File emptyFile = new File("src/test/java/emptydata.csv");
        man = new FileBackedTaskManager(file);

        emptyManager = new FileBackedTaskManager(emptyFile);
        initTasks();
    }

    @Test
    public void readFromFile(){
        assertTrue(emptyManager.getAllTasks().isEmpty(), "Вовращает не пустой список");

        emptyManager = emptyManager.readFromFile();
        assertTrue(emptyManager.getAllTasks().isEmpty(), "Вовращает не пустой список");
        assertTrue(emptyManager.historyManager.getHistory().isEmpty(),"Возвращает не пустой список истории");

        man.addTask(task);
        man.getTask(1);

        FileBackedTaskManager taskManager2 = new FileBackedTaskManager(file).readFromFile();
        final List<Task> tasks = taskManager2.getAllTasks();
        assertNotNull(tasks, "Вовращает пустой список");
        assertEquals(man.getTask(1), taskManager2.getTask(1), "Возращает неодинаковые задачи");
        assertEquals(1,tasks.size(), "Возвращает неверный список");
        //assertNotNull(taskManager2.getPrioritizedTasks(),"Возвращает пустой список");
        assertEquals(man.historyManager.getHistory().size(),taskManager2.historyManager.getHistory().size(), "Возвращает неверную историю");
    }



}