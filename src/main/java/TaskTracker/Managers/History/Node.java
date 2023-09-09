package TaskTracker.Managers.History;

import TaskTracker.Tasks.Task;

import java.util.Objects;

public class Node<T extends Task> {
    Node<T> next;
    Node<T> prev;
    Task task;

    public Node(Task task) {
        this.task = task;
    }

    @Override
    public int hashCode() {
        return Objects.hash(task);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Node<?> node = (Node<?>) obj;
        return Objects.equals(node.task, task);
    }
}
