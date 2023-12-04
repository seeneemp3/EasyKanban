package TaskTracker;

import TaskTracker.http.HttpTaskServer;
import TaskTracker.http.KVServer;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        var KVServ = new KVServer();
        KVServ.start();

        var serv = new HttpTaskServer();
        serv.start();

    }
}