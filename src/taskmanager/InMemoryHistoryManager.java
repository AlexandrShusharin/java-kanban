package taskmanager;

import task.*;

import java.util.List;
import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history;

    public InMemoryHistoryManager() {
        history = new LinkedList<Task>();
    }

    @Override
    public void add(Task task) {
        if (history.size() > 9) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
