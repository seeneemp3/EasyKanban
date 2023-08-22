package TaskTracker.TaskExeptions;

public class InsertTaskExeption extends IllegalArgumentException {
    public InsertTaskExeption(String msg){
        super(msg);
    }
}
