package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String backedFileName = "BackedTasksManager.csv";

    public FileBackedTasksManager() {
        super();
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
        return super.getEpic(taskId);
    }

    @Override
    public Task getTask(int taskId) {
        return super.getTask(taskId);
    }

    @Override
    public Subtask getSubtask(int taskId) {
        return super.getSubtask(taskId);
    }

    @Override
    public List<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return super.getAllSubtask();
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

    }

    private Path getBackedFilePath() {
        return null;
    }
}
