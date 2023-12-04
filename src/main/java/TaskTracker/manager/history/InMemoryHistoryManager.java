package TaskTracker.manager.history;

import TaskTracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> nodesMap = new HashMap<>();
    protected CustomList<Task> linkedTaskList = new CustomList<>();

    @Override
    public List<Task> getHistory() {
        return linkedTaskList.getTasks();
    }

    @Override
    public void addTask(Task task) throws NullPointerException {
        Node<Task> node = null;
        if (task != null) {
            node = linkedTaskList.linkLast(task);
        }
        if (nodesMap.containsKey(task.getId())) {
            linkedTaskList.removeNode(nodesMap.get(task.getId()));
        }
        nodesMap.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        if (nodesMap.containsKey(id)) {
            linkedTaskList.removeNode(nodesMap.get(id));
            nodesMap.remove(id);
        }
    }

    @Override
    public String toString() {
        List<Task> history = getHistory();
        return history.stream()
                .map(Task::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private static class CustomList<T extends Task> {
        Node<T> head;
        Node<T> tail;
        private int size = 0;

        private Node<T> linkLast(Task task) {
            Node<T> newNode = new Node<T>(task);
            if (size == 0) {
                newNode.next = null;
                newNode.prev = null;
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;
            size++;

            return tail;
        }

        private List<Task> getTasks() {
            List<Task> taskList = new ArrayList<>();
            Node<T> node = head;
            Node<T> node2 = tail;
            while (node != null) {
                taskList.add(node.task);
                node = node.next;
            }
            return taskList;
        }

        private void removeNode(Node<T> node) {
            if (node.next != null) {
                node.next.prev = node.prev;
            } else tail = node.prev;
            if (node.prev != null) {
                node.prev.next = node.next;
            } else head = node.next;
            size--;
        }
    }


}
