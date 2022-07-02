package TaskManager;

import Task.Epic;
import Task.Task;
import Task.SubTask;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public interface TaskManager {

    //методы добавления задач
    public void addEpic (Epic epic);
    public void addTask (Task task);
    public void addSubTask (SubTask subTask);

    //методы обновления задач
    public void updateEpic (Epic epic);
    public void updateTask (Task task);
    public void updateSubTask (SubTask subTask);

    //методы удаления задач пл id (по типу)
    public void removeEpic (int taskId);
    public void removeTask (int taskId);
    public void removeSubTask (int taskId);

    //методы получения задач по id (по типу)
    public Epic getEpic (int taskId);
    public Task getTask (int taskId);
    public SubTask getSubTask (int taskId);

    //методоы получения всех задач в классе (по типу)
    public Collection<Epic> getAllEpic();
    public Collection<SubTask> getAllSubTask();
    public Collection<Task> getAllTask();

    //получение подзадач эпика (вощвращает коллекцию id подзадач)
    public ArrayList<Integer> getSubTasksInEpic(int epicId);

    //методы удаления всех задач по типу (по типу)
    public void removeAllEpic ();
    public void removeAllTask ();
    public void removeAllSubTask ();

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    public void updateEpicStatus(int epicId);

    //Генератор уникального номера
    public int getNewTaskId ();

    public List<Task> getHistory();
}
