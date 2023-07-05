package org.example.Managers;

import org.example.Managers.History.HistoryManager;
import org.example.Managers.History.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory(){
        return  new InMemoryHistoryManager();
    }
}
