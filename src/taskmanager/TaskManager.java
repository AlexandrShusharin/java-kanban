package taskmanager;

import task.Epic;
import task.Task;
import task.Subtask;

import java.util.List;


public interface TaskManager {

    //методы добавления задач
    public void addEpic (Epic epic);

    public void addTask (Task task);

    public void addSubtask (Subtask subTask);

    //методы обновления задач
    public void updateEpic (Epic epic);

    public void updateTask (Task task);

    public void updateSubtask (Subtask subTask);

    //методы удаления задач пл id (по типу)
    public void removeEpic (int taskId);

    public void removeTask (int taskId);

    public void removeSubtask (int taskId);

    //методы получения задач по id (по типу)
    public Epic getEpic (int taskId);

    public Task getTask (int taskId);

    public Subtask getSubtask (int taskId);

    //методоы получения всех задач в классе (по типу)
    public List<Epic> getAllEpic();

    public List<Subtask> getAllSubtask();

    public List<Task> getAllTask();

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    public List<Subtask> getEpicSubtasks(int epicId);

    //методы удаления всех задач по типу (по типу)
    public void removeAllEpic ();

    public void removeAllTask ();

    public void removeAllSubtask ();

    //получение истории просмотра задач
    public List<Task> getHistory();
}
