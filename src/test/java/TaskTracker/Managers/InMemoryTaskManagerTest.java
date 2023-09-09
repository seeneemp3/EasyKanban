package TaskTracker.Managers;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setup() {
        man = new InMemoryTaskManager();
        initTasks();
    }

}