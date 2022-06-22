import java.util.Collection;
import java.util.HashMap;

public class Epic extends Task {
    protected HashMap<Integer, SubTask> subTasks;

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
        subTasks = new HashMap<Integer, SubTask>();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        this.updateStatus();
    }
    public void updateSubTask(SubTask subTask) {

        subTasks.put(subTask.getId(), subTask);
        this.updateStatus();
    }
    public void removeSubTask(int id) {
        subTasks.remove(id);
        this.updateStatus();
    }
    public void removeAllSubTask() {
        subTasks.clear();
        this.updateStatus();
    }
    public Collection<SubTask> getAllSubTask() {
        return subTasks.values();
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + description.length() + '\'' +
                ", status='" + status + '\'' +
                ", subTasks: [";
        boolean isFirstSubTask = true;
        for (SubTask subTask : subTasks.values()) {
            if (!isFirstSubTask) {
                result += ", ";
            }
            else {
                isFirstSubTask = false;
            }
            result += subTask;
        }
        result += "]}";
        return result;
    }

    //метод для обновления статуса эпика (проходит по всем подзадачам эпика)
    public void updateStatus() {
        //NEW DONE IN_PROGRESS
        int taskDoneCounter = 0;
        int taskNewCounter = 0;
        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStatus().equals("NEW")) {
                taskNewCounter++;
            }
            else if (subTask.getStatus().equals("DONE")) {
                taskDoneCounter++;
            }
        }
        if (taskNewCounter == subTasks.size()) {
            this.status = "NEW";
        }
        else if (taskDoneCounter == subTasks.size()) {
            this.status = "DONE";
        }
        else  {
            this.status =  "IN_PROGRESS";
        }
    }
}
