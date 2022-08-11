package taskmanager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String backedFileName;

    public FileBackedTasksManager(String backedFileName) {
        super();
        this.backedFileName = backedFileName;
        if (isBackedFileExist()) {
            loadFromFile(Paths.get(backedFileName).toFile());
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

    public void save() throws ManagerSaveException {
       try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(backedFileName))) {
           bufferedWriter.write("id,type,name,status,description,epic\n");
           for (Integer taskId : tasks.keySet()) {
               bufferedWriter.write(tasks.get(taskId).toString()+"\n");
           }
           for (Integer epicId : epics.keySet()) {
               bufferedWriter.write(epics.get(epicId).toString()+"\n");
           }
           for (Integer subtaskId : subtasks.keySet()) {
               bufferedWriter.write(subtasks.get(subtaskId).toString()+"\n");
           }
           bufferedWriter.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }

    }

    public void loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String[] fileLines =  bufferedReader.lines().toArray(String[]::new);
            for (int i = 0; i < fileLines.length; i++) {
                if ((i > 0) && (i < fileLines.length - 2)) {
                    String[] valueArr = fileLines[i].split(",");
                    if (valueArr[1].equals(TaskType.TASK.toString())) {
                        Task task = new Task(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]));
                        task.setId(Integer.parseInt(valueArr[0]));
                        tasks.put(Integer.parseInt(valueArr[0]), task);
                     } else if (valueArr[1].equals(TaskType.EPIC.toString())) {
                        Epic epic = new Epic(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]));
                        epic.setId(Integer.parseInt(valueArr[0]));
                        epics.put(Integer.parseInt(valueArr[0]), epic);
                    } else if (valueArr[1].equals(TaskType.SUBTASK.toString())) {
                        Subtask subtask = new Subtask(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]),
                                Integer.parseInt(valueArr[5]));
                        subtask.setId(Integer.parseInt(valueArr[0]));
                        epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
                        subtasks.put(Integer.parseInt(valueArr[0]), subtask);
                    }
                } else if (i == (fileLines.length - 1)) {
                    for (Integer taskId: historyFromString(fileLines[i])) {
                        if (tasks.containsKey(taskId)){
                            historyManager.add(tasks.get(taskId));
                        }
                        else if (subtasks.containsKey(taskId)) {
                            historyManager.add(subtasks.get(taskId));
                        }
                        else if (epics.containsKey(taskId)) {
                            historyManager.add(epics.get(taskId));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    static List<Integer> historyFromString(String value) {
        ArrayList<Integer> history = new ArrayList<>();
        String[] arr = value.split(",");
        for (int i = 0; i < arr.length; i++) {
            history.add(Integer.parseInt(arr[i]));
        }
        return history;
    }
}
