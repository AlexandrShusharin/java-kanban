package TaskManager;

import Task.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    protected HashMap<Integer, Task> taskList;
    protected HashMap<Integer, Epic> epicList;
    protected HashMap<Integer, SubTask> subTaskList;
    private int lastTaskId;

    public TaskManager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subTaskList = new HashMap<>();
        this.lastTaskId = 0;
    }

    //методы добавления задач (по типу)
    public void addEpic (Epic epic) {
        epicList.put(epic.getId(), epic);
    }
    public void addTask (Task task) {
        taskList.put(task.getId(), task);
    }
    public void addSubTask (SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        epicList.get(subTask.getEpicId()).addSubTask(subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    //методы обновления задач
    public void updateEpic (Epic epic) {
        epicList.put(epic.getId(), epic);
    }
    public void updateTask (Task task) {
        taskList.put(task.getId(), task);
    }
    public void updateSubTask (SubTask subTask) {
        subTaskList.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    //методы удаления задач пл id (по типу)
    public void removeEpic (int taskId) {
        for (Integer subTaskId : epicList.get(taskId).getAllSubTask()) {
            subTaskList.remove(subTaskId);
        }
        epicList.remove(taskId);
    }
    public void removeTask (int taskId) {
        taskList.remove(taskId);
    }
    public void removeSubTask (int taskId) {
        epicList.get(subTaskList.get(taskId).getEpicId()).removeSubTask(taskId);
        updateEpicStatus(subTaskList.get(taskId).getEpicId());
        subTaskList.remove(taskId);
    }

    //методы получения задач по id (по типу)
    public Epic getEpic (int taskId) {
        return epicList.get(taskId);
    }
    public Task getTask (int taskId) {
        return taskList.get(taskId);
    }
    public SubTask getSubTask (int taskId) {
        return subTaskList.get(taskId);
    }

    //методоы получения всех задач в классе (по типу)
    public Collection<Epic> getAllEpic() {
        return epicList.values();
    }
    public Collection<SubTask> getAllSubTask() {
        return subTaskList.values();
    }
    public Collection<Task> getAllTask() {
        return taskList.values();
    }

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    public ArrayList<Integer> getSubTasksInEpic(int epicId) { return epicList.get(epicId).getAllSubTask(); }

    //методы удаления всех задач по типу (по типу)
    public void removeAllEpic () {
        epicList.clear();
        subTaskList.clear();
    }
    public void removeAllTask () {
        taskList.clear();
    }
    public void removeAllSubTask () {
        subTaskList.clear();
        for (Epic epic : epicList.values()) {
            epic.removeAllSubTask();
            updateEpicStatus(epic.getId());
        }
    }

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    public void updateEpicStatus(int epicId) {
        //NEW DONE IN_PROGRESS
        int taskDoneCounter = 0;
        int taskNewCounter = 0;
        for (Integer subTaskId : epicList.get(epicId).getAllSubTask()) {
            if (subTaskList.get(subTaskId).getStatus().equals("NEW")) {
                taskNewCounter++;
            }
            else if (subTaskList.get(subTaskId).getStatus().equals("DONE")) {
                taskDoneCounter++;
            }
        }
        if (taskNewCounter == epicList.get(epicId).getAllSubTask().size()) {
            epicList.get(epicId).setStatus("NEW");
        }
        else if (taskDoneCounter == epicList.get(epicId).getAllSubTask().size()) {
            epicList.get(epicId).setStatus("DONE");
        }
        else  {
            epicList.get(epicId).setStatus("IN_PROGRESS");
        }
    }

    //Генератор уникального номера
    public int getNewTaskId () {
        this.lastTaskId++;
        return lastTaskId;
    }

}
