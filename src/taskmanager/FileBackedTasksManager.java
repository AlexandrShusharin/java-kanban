package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String backedFileName;

    public FileBackedTasksManager(String backedFileName) {
        super();
        this.backedFileName = backedFileName;
        if (isBackedFileExist()) {
            //;
        }
        else {
            createBackedFile();
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void removeEpic(int taskId) {
        super.removeEpic(taskId);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeSubtask(int taskId) {
        super.removeSubtask(taskId);
        save();
    }

    @Override
    public Epic getEpic(int taskId) {
        Epic epic = super.getEpic(taskId);
        save();
        return epic;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = super.getTask(taskId);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int taskId) {
        Subtask subtask = super.getSubtask(taskId);
        save();
        return subtask;
    }

    @Override
    public List<Epic> getAllEpic() {

        List<Epic> allEpic =  super.getAllEpic();
        save();
        return allEpic;
    }

    @Override
    public List<Subtask> getAllSubtask() {
        List<Subtask> allSubtask = super.getAllSubtask();
        save();
        return allSubtask;
    }

    @Override
    public List<Task> getAllTask() {
        return super.getAllTask();
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        return super.getEpicSubtasks(epicId);
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public void save() {
       try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(backedFileName))) {
           bufferedWriter.write("id,type,name,status,description,epic\n");
           for (Integer taskId : tasks.keySet()) {
               bufferedWriter.write(tasks.get(taskId).toString()+"\n");
           }
           for (Integer subtaskId : subtasks.keySet()) {
               bufferedWriter.write(subtasks.get(subtaskId).toString()+"\n");
           }
           for (Integer epicId : epics.keySet()) {
               bufferedWriter.write(epics.get(epicId).toString()+"\n");
           }
           bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadFromFile() {

    }

    private boolean isBackedFileExist() {
        Path backedFilePath = Paths.get(backedFileName);
        if (Files.exists(backedFilePath)) {
            return true;
        } else {
            return false;
        }
    }

    private void createBackedFile() {
        Path backedFilePath = Paths.get(backedFileName);
        try {
            Files.createFile(backedFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        String result = "\n";
        for (Task task : history) {
            if (result.isBlank()) {
                result += task.getId();
            } else {
                result += "," + task.getId();
            }
        }
        return result;
    }
}
