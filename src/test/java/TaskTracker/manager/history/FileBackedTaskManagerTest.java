package TaskTracker.manager.history;

import TaskTracker.manager.TaskManagerTest;
import TaskTracker.task.Task;
import org.junit.jupiter.api.BeforeEach;
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
    public void readFromFile() {
        assertTrue(emptyManager.getAllTasks().isEmpty(), "Returns a non-empty list");

        emptyManager = emptyManager.readFromFile();
        assertTrue(emptyManager.getAllTasks().isEmpty(), "Returns a non-empty list");
        assertTrue(emptyManager.historyManager.getHistory().isEmpty(), "Returns a non-empty history list");

        man.addTask(task);
        man.getTask(1);

        FileBackedTaskManager taskManager2 = new FileBackedTaskManager(file).readFromFile();
        final List<Task> tasks = taskManager2.getAllTasks();
        assertNotNull(tasks, "Returns an empty list");
        assertEquals(man.getTask(1), taskManager2.getTask(1), "Returns different tasks");
        assertEquals(1, tasks.size(), "Returns an incorrect list");
        assertEquals(man.historyManager.getHistory().size(), taskManager2.historyManager.getHistory().size(), "Returns incorrect history");
    }


}