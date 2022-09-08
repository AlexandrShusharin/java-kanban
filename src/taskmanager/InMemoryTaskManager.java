package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    TreeMap<LocalDateTime, Task> prioritizedTasks;
    protected HistoryManager historyManager;

    protected int lastTaskId;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.prioritizedTasks = new TreeMap<>();
        this.lastTaskId = 0;
        this.historyManager = Managers.getDefaultHistory();
    }

    //методы добавления задач (по типу)
    @Override
    public void addEpic(Epic epic) {
        epic.setId(getNewTaskId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addTask(Task task) {
        if (isTaskTimeNotOccupied(task)) {
            task.setId(getNewTaskId());
            addToPrioritizedTasks(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isTaskTimeNotOccupied(subtask)) {
            subtask.setId(getNewTaskId());
            subtasks.put(subtask.getId(), subtask);
            addToPrioritizedTasks(subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask.getId());
            updateEpicStatus(subtask.getEpicId());
            updateEpicStartEndTime(subtask.getEpicId());
        }
    }

    //методы обновления задач
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateTask(Task task) {
        if (isTaskTimeNotOccupied(task)) {
            updatePrioritizedTasks(task);
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isTaskTimeNotOccupied(subtask)) {
            prioritizedTasks.put(subtask.getStartTime(), subtask);
            updatePrioritizedTasks(subtask);
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
            updateEpicStartEndTime(subtask.getEpicId());
        }
    }

    //методы удаления задач пл id (по типу)
    @Override
    public void removeEpic(int taskId) {
        for (Integer subTaskId : epics.get(taskId).getAllSubTask()) {
            historyManager.remove(subTaskId);
            removeFromPrioritizedTasks(subtasks.get(subTaskId));
            subtasks.remove(subTaskId);
        }
        historyManager.remove(taskId);
        epics.remove(taskId);
    }

    @Override
    public void removeTask(int taskId) {
        removeFromPrioritizedTasks(tasks.get(taskId));
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeSubtask(int taskId) {
        prioritizedTasks.remove(subtasks.get(taskId).getStartTime());
        historyManager.remove(taskId);
        epics.get(subtasks.get(taskId).getEpicId()).removeSubtask(taskId);
        updateEpicStatus(subtasks.get(taskId).getEpicId());
        updateEpicStartEndTime(subtasks.get(taskId).getEpicId());
        removeFromPrioritizedTasks(subtasks.get(taskId));
        subtasks.remove(taskId);
    }

    //методы получения задач по id (по типу)
    @Override
    public Epic getEpic(int taskId) {
        if (epics.containsKey(taskId)) {
            historyManager.add(epics.get(taskId));
            return epics.get(taskId);
        } else {
            return null;
        }
    }

    @Override
    public Task getTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            historyManager.add(tasks.get(taskId));
            return tasks.get(taskId);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int taskId) {
        if (subtasks.containsKey(taskId)) {
            historyManager.add(subtasks.get(taskId));
            return subtasks.get(taskId);
        } else {
            return null;
        }
    }

    //методоы получения всех задач в классе (по типу)

    @Override
    public List<Epic> getAllEpic() {
        return new ArrayList<Epic>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return new ArrayList<Subtask>(subtasks.values());
    }

    @Override
    public List<Task> getAllTask() {
        return new ArrayList<Task>(tasks.values());
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
    public void removeAllEpic() {
        List<Integer> epicsId = new ArrayList<>(epics.keySet());
        for (int id : epicsId) {
            removeEpic(id);
        }
    }

    @Override
    public void removeAllTask() {
        List<Integer> tasksId = new ArrayList<>(tasks.keySet());
        for (int id : tasksId) {
            removeTask(id);
        }
    }

    @Override
    public void removeAllSubtask() {
        List<Integer> subtasksId = new ArrayList<>(subtasks.keySet());
        for (int id : subtasksId) {
            removeSubtask(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks.values());
    }

    //метод для обновления времени эпика
    private void updateEpicStartEndTime(int epicId) {
        if (epics.get(epicId).getAllSubTask().size() > 0) {
            TreeMap<LocalDateTime, Task> epicSubtasksByStart = new TreeMap<>();
            TreeMap<LocalDateTime, Task> epicSubtasksByEnd = new TreeMap<>();
            for (Integer subTaskId : epics.get(epicId).getAllSubTask()) {
                epicSubtasksByStart.put(subtasks.get(subTaskId).getStartTime(), subtasks.get(subTaskId));
                epicSubtasksByEnd.put(subtasks.get(subTaskId).getEndTime().
                        plusMinutes(subtasks.get(subTaskId).getDuration()), subtasks.get(subTaskId));
            }
            epics.get(epicId).setStartTime(epicSubtasksByStart.firstKey());
            epics.get(epicId).setEndTime(epicSubtasksByEnd.lastKey());
        }
    }

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    private void updateEpicStatus(int epicId) {
        //NEW DONE IN_PROGRESS
        int taskDoneCounter = 0;
        int taskNewCounter = 0;
        for (Integer subTaskId : epics.get(epicId).getAllSubTask()) {
            if (subtasks.get(subTaskId).getStatus() == TaskStatus.NEW) {
                taskNewCounter++;
            } else if (subtasks.get(subTaskId).getStatus() == TaskStatus.DONE) {
                taskDoneCounter++;
            }
        }
        if (taskNewCounter == epics.get(epicId).getAllSubTask().size()) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
        } else if (taskDoneCounter == epics.get(epicId).getAllSubTask().size()) {
            epics.get(epicId).setStatus(TaskStatus.DONE);
        } else {
            epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }


    private boolean isTaskTimeNotOccupied(Task task) {
         if (prioritizedTasks.containsKey(task.getStartTime())) {
            if (prioritizedTasks.get(task.getStartTime()).getId() == task.getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private void addToPrioritizedTasks(Task task) {
        prioritizedTasks.put(task.getStartTime(), task);
    }

    private void updatePrioritizedTasks(Task task) {
        prioritizedTasks.put(task.getStartTime(), task);
    }

    private void removeFromPrioritizedTasks(Task task) {
        prioritizedTasks.remove(task.getStartTime());
    }

    //Генератор уникального номера
    private int getNewTaskId() {
        this.lastTaskId++;
        return lastTaskId;
    }

    protected void setLsatTaskId(int value) {
        this.lastTaskId = value;
    }
}