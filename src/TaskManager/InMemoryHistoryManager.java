package TaskManager;

import Task.*;

import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> taskHistoryList;

    public InMemoryHistoryManager() {
        taskHistoryList = new ArrayList<Task>();
    }

    @Override
    public void add(Task task) {
        if (taskHistoryList.size() >= 10) {
            taskHistoryList.remove(0);
        }
        taskHistoryList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskHistoryList;
    }
}
