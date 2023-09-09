package TaskTracker;

import TaskTracker.Http.HttpTaskServer;
import TaskTracker.Http.KVServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        var KVServ = new KVServer();
        KVServ.start();

        var serv = new HttpTaskServer();
        serv.start();

    }
}