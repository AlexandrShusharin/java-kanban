package taskmanager;

import task.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    protected HistoryManager historyManager;
    private int lastTaskId;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.lastTaskId = 0;
        this.historyManager = Managers.getDefaultHistory();
    }

    //методы добавления задач (по типу)
    @Override
    public void addEpic (Epic epic) {
        epic.setId(getNewTaskId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addTask (Task task) {
        task.setId(getNewTaskId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addSubtask (Subtask subtask) {
        subtask.setId(getNewTaskId());
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask);
        updateEpicStatus(subtask.getEpicId());
    }

    //методы обновления задач
    @Override
    public void updateEpic (Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask (Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask (Subtask subTask) {
        subtasks.put(subTask.getId(), subTask);
        updateEpicStatus(subTask.getEpicId());
    }

    //методы удаления задач пл id (по типу)
    @Override
    public void removeEpic (int taskId) {
        for (Integer subTaskId : epics.get(taskId).getAllSubTask()) {
            historyManager.remove(subTaskId);
            subtasks.remove(subTaskId);
        }
        historyManager.remove(taskId);
        epics.remove(taskId);
    }

    @Override
    public void removeTask (int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeSubtask (int taskId) {
        historyManager.remove(taskId);
        epics.get(subtasks.get(taskId).getEpicId()).removeSubtask(taskId);
        updateEpicStatus(subtasks.get(taskId).getEpicId());
        subtasks.remove(taskId);
    }

    //методы получения задач по id (по типу)
    @Override
    public Epic getEpic (int taskId) {
        historyManager.add(epics.get(taskId));
        return epics.get(taskId);
    }

    @Override
    public Task getTask (int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public Subtask getSubtask (int taskId) {
        historyManager.add(subtasks.get(taskId));
        return subtasks.get(taskId);
    }

    //методоы получения всех задач в классе (по типу)

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList(subtasks.values());
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList(tasks.values());
    }

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (int subtaskId : epics.get(epicId).getAllSubTask()) {
            epicSubtasks.add(subtasks.get(subtaskId));
        }
        return epicSubtasks;
    }

    //методы удаления всех задач по типу (по типу)
    @Override
    public void removeAllEpic () {
//     Предыдущая реализация без очитски истории
//      epics.clear();
//      subtasks.clear();

        for (int id : epics.keySet()) {
            removeEpic(id);
        }
    }

    @Override
    public void removeAllTask () {
//      Предыдущая реализация без очитски истории
//      tasks.clear();

        for (int id : tasks.keySet()) {
            removeTask(id);
        }
    }

    @Override
    public void removeAllSubtask () {
//      Предыдущая реализация без очитски истории
//        subtasks.clear();
//        for (Epic epic : epics.values()) {
//            epic.removeAllSubtask();
//            updateEpicStatus(epic.getId());
//        }
        List<Integer> subtasksId = new ArrayList<>(subtasks.keySet());
        for (int id :subtasksId) {
            removeSubtask(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    private void updateEpicStatus(int epicId) {
        //NEW DONE IN_PROGRESS
        int taskDoneCounter = 0;
        int taskNewCounter = 0;
        for (Integer subTaskId : epics.get(epicId).getAllSubTask()) {
            if (subtasks.get(subTaskId).getStatus() == TaskStatus.NEW) {
                taskNewCounter++;
            }
            else if (subtasks.get(subTaskId).getStatus() == TaskStatus.DONE) {
                taskDoneCounter++;
            }
        }
        if (taskNewCounter == epics.get(epicId).getAllSubTask().size()) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
        }
        else if (taskDoneCounter == epics.get(epicId).getAllSubTask().size()) {
            epics.get(epicId).setStatus(TaskStatus.DONE);
        }
        else  {
            epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //Генератор уникального номера
    private int getNewTaskId () {
        this.lastTaskId++;
        return lastTaskId;
    }

}
