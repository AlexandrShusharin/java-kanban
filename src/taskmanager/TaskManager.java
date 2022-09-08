package taskmanager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;


public interface TaskManager {

    //методы добавления задач
    void addEpic(Epic epic);

    void addTask(Task task);

    void addSubtask(Subtask subTask);

    //методы обновления задач
    void updateEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subTask);

    //методы удаления задач пл id (по типу)
    void removeEpic(int taskId);

    void removeTask(int taskId);

    void removeSubtask(int taskId);

    //методы получения задач по id (по типу)
    Epic getEpic(int taskId);

    Task getTask(int taskId);

    Subtask getSubtask(int taskId);

    //методоы получения всех задач в классе (по типу)
    List<Epic> getAllEpic();

    List<Subtask> getAllSubtask();

    List<Task> getAllTask();

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    List<Subtask> getEpicSubtasks(int epicId);

    //методы удаления всех задач по типу (по типу)
    void removeAllEpic();

    void removeAllTask();

    void removeAllSubtask();

    //получение истории просмотра задач
    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
