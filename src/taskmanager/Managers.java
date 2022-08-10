package taskmanager;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTasksManager("taskManagerBase.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
