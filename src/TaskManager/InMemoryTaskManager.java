package TaskManager;

import Task.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> taskList;
    protected HashMap<Integer, Epic> epicList;
    protected HashMap<Integer, SubTask> subTaskList;
    protected HistoryManager historyManager;
    private int lastTaskId;

    public InMemoryTaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subTaskList = new HashMap<>();
        this.lastTaskId = 0;
        this.historyManager = Managers.getDefaultHistory();
    }

    //методы добавления задач (по типу)
    @Override
    public void addEpic (Epic epic) {
        epicList.put(epic.getId(), epic);
    }
    @Override
    public void addTask (Task task) {
        taskList.put(task.getId(), task);
    }
    @Override
    public void addSubTask (SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        epicList.get(subTask.getEpicId()).addSubTask(subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    //методы обновления задач
    @Override
    public void updateEpic (Epic epic) {
        epicList.put(epic.getId(), epic);
    }
    @Override
    public void updateTask (Task task) {
        taskList.put(task.getId(), task);
    }
    @Override
    public void updateSubTask (SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    //методы удаления задач пл id (по типу)
    @Override
    public void removeEpic (int taskId) {
        for (Integer subTaskId : epicList.get(taskId).getAllSubTask()) {
            subTaskList.remove(subTaskId);
        }
        epicList.remove(taskId);
    }
    @Override
    public void removeTask (int taskId) {
        taskList.remove(taskId);
    }
    @Override
    public void removeSubTask (int taskId) {
        epicList.get(subTaskList.get(taskId).getEpicId()).removeSubTask(taskId);
        updateEpicStatus(subTaskList.get(taskId).getEpicId());
        subTaskList.remove(taskId);
    }

    //методы получения задач по id (по типу)
    @Override
    public Epic getEpic (int taskId) {
        historyManager.add(epicList.get(taskId));
        return epicList.get(taskId);
    }
    @Override
    public Task getTask (int taskId) {
        historyManager.add(taskList.get(taskId));
        return taskList.get(taskId);
    }
    @Override
    public SubTask getSubTask (int taskId) {
        historyManager.add(subTaskList.get(taskId));
        return subTaskList.get(taskId);
    }

    //методоы получения всех задач в классе (по типу)
    @Override
    public Collection<Epic> getAllEpic() {
        return epicList.values();
    }
    @Override
    public Collection<SubTask> getAllSubTask() {
        return subTaskList.values();
    }
    @Override
    public Collection<Task> getAllTask() {
        return taskList.values();
    }

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    @Override
    public ArrayList<Integer> getSubTasksInEpic(int epicId) { return epicList.get(epicId).getAllSubTask(); }

    //методы удаления всех задач по типу (по типу)
    @Override
    public void removeAllEpic () {
        epicList.clear();
        subTaskList.clear();
    }
    @Override
    public void removeAllTask () {
        taskList.clear();
    }
    @Override
    public void removeAllSubTask () {
        subTaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.removeAllSubTask();
            updateEpicStatus(epic.getId());
        }
    }

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    @Override
    public void updateEpicStatus(int epicId) {
        //NEW DONE IN_PROGRESS
        int taskDoneCounter = 0;
        int taskNewCounter = 0;
        for (Integer subTaskId : epicList.get(epicId).getAllSubTask()) {
            if (subTaskList.get(subTaskId).getStatus() == TaskStatus.NEW) {
                taskNewCounter++;
            }
            else if (subTaskList.get(subTaskId).getStatus() == TaskStatus.DONE) {
                taskDoneCounter++;
            }
        }
        if (taskNewCounter == epicList.get(epicId).getAllSubTask().size()) {
            epicList.get(epicId).setStatus(TaskStatus.NEW);
        }
        else if (taskDoneCounter == epicList.get(epicId).getAllSubTask().size()) {
            epicList.get(epicId).setStatus(TaskStatus.DONE);
        }
        else  {
            epicList.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //Генератор уникального номера
    @Override
    public int getNewTaskId () {
        this.lastTaskId++;
        return lastTaskId;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
