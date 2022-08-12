package taskmanager;

import java.nio.file.Paths;

public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.loadFromFile(Paths.get("taskManagerBase.csv").toFile());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
