package TaskTracker;

import TaskTracker.Http.HTTPTaskManager;
import TaskTracker.Http.HttpTaskServer;
import TaskTracker.Http.KVServer;
import TaskTracker.Http.KVTaskClient;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        var KVServ = new KVServer();
        KVServ.start();
        var serv = new HttpTaskServer();
        serv.start();

    }
}