package taskmanager;

import task.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private class Node {
        public Node next;
        public Node prev;
        public Task task;

        public Node(Task task, Node next, Node prev) {
            this.next = next;
            this.prev = prev;
            this.task = task;
        }
    }

    private HashMap<Integer, Node> history;
    private Node firstNode;
    private Node lastNode;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    private Node linkLast(Task task) {
        Node node = new Node(task, null, lastNode);
        if (firstNode == null) {
            firstNode = node;
        } else {
            lastNode.next = node;
        }
        lastNode = node;
        return node;
    }

    private void removeNode(Node node) {
        Node nextNode = node.next;
        Node prevNode = node.prev;
        if (prevNode == null && nextNode == null) {
            firstNode = null;
            lastNode = null;
        } else if (prevNode == null) {
            nextNode.prev = null;
            firstNode = nextNode;
        } else if (nextNode == null) {
            prevNode.next = null;
            lastNode = prevNode;
        } else {
            prevNode.next = nextNode;
            nextNode.prev = prevNode;
        }
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        history.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskHistory = new ArrayList<>();
        Node node = firstNode;
        while (node != null) {
            taskHistory.add(node.task);
            node = node.next;
        }
        return taskHistory;
    }
}
