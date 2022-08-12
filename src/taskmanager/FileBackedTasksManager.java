package taskmanager;

import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String backedFileName;

    public FileBackedTasksManager(String backedFileName) {
        super();
        this.backedFileName = backedFileName;
        if (!isBackedFileExist()) {
            createBackedFile();
        }
    }

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new FileBackedTasksManager("taskManagerBase.csv");

        //Блок тестирования

        //создаем тестовые объекты
        Task task1 = new Task("Задача 1", "Постричь кусты", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Полить газон", TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Посадить цветы", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Купить семяна", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Вскопать гядку", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Полить", TaskStatus.NEW, epic1.getId());
        taskManager.addSubtask(subtask3);
        Epic epic2 = new Epic("Эпик 2", "Пожарить шашлык", TaskStatus.NEW);
        taskManager.addEpic(epic2);

        //накрутка просмотров
        for (Task task : taskManager.getAllTask()) {
            taskManager.getTask(task.getId());
        }
        for (Task task : taskManager.getAllEpic()) {
            taskManager.getEpic(task.getId());
        }
        for (Task task : taskManager.getAllSubtask()) {
            taskManager.getSubtask(task.getId());
        }

        //Загрузка из файла
        taskManager = FileBackedTasksManager.loadFromFile(Paths.get("taskManagerBase.csv").toFile());

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());


        System.out.println();


        //проверка истории
        System.out.println();
        System.out.println("История просмотров: ");
        System.out.println(taskManager.getHistory());
        System.out.println("--------------Окончание истории-----------------");
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

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getPath().toString());
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String[] fileLines =  bufferedReader.lines().toArray(String[]::new);
            List<Integer> allId = new ArrayList<>();
            for (int i = 0; i < fileLines.length; i++) {
                if ((i > 0) && (i < fileLines.length - 2)) {
                    Task task = taskFromString(fileLines[i]);
                    if (task instanceof Epic) {
                        fileBackedTasksManager.updateEpic((Epic) task);
                        allId.add(task.getId());
                    } else if (task instanceof Subtask) {
                        fileBackedTasksManager.updateSubtask((Subtask) task);
                        allId.add(task.getId());
                    } else if (task instanceof Task) {
                        fileBackedTasksManager.updateTask(task);
                        allId.add(task.getId());
                    }
                } else if (i == (fileLines.length - 1)) {
                    for (Integer taskId: historyFromString(fileLines[i])) {
                        if (fileBackedTasksManager.getTask(taskId) != null){
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getTask(taskId));
                        }
                        else if (fileBackedTasksManager.getSubtask(taskId) != null) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getSubtask(taskId));
                        }
                        else if (fileBackedTasksManager.getEpic(taskId) != null) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getEpic(taskId));
                        }
                    }
                }
            }
            if (allId.size() > 0) {
                fileBackedTasksManager.setStartTaskId(Collections.max(allId));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileBackedTasksManager;
    }
    static private Task taskFromString(String value) {
        String[] valueArr = value.split(",");
        if (valueArr[1].equals(TaskType.TASK.toString())) {
            Task task = new Task(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]));
            task.setId(Integer.parseInt(valueArr[0]));
            return task;
        } else if (valueArr[1].equals(TaskType.EPIC.toString())) {
            Epic epic = new Epic(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]));
            epic.setId(Integer.parseInt(valueArr[0]));
            return epic;
        } else if (valueArr[1].equals(TaskType.SUBTASK.toString())) {
            Subtask subtask = new Subtask(valueArr[2], valueArr[4], TaskStatus.valueOf(valueArr[3]),
                    Integer.parseInt(valueArr[5]));
            subtask.setId(Integer.parseInt(valueArr[0]));
            return subtask;
        } else {
            return null;
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
        if (!value.isBlank()) {
            String[] arr = value.split(",");
            for (int i = 0; i < arr.length; i++) {
                history.add(Integer.parseInt(arr[i]));
            }
        }
        return history;
    }
}
