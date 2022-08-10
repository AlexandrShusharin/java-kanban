package task;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTasks;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subTask) {
        subTasks.add(subTask.getId());
    }

    public void removeSubtask(Integer id) {
        subTasks.remove(id);
    }

    public void removeAllSubtask() {
        subTasks.clear();
    }

    public ArrayList<Integer> getAllSubTask() {
        return subTasks;
    }

    @Override
    public String toString() {
        return  id +
                "," + TaskType.EPIC +
                "," + name +
                "," + status +
                "," + description +
                "," ;
    }
/*
        String result = "Task.Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.length='" + description.length() + '\'' +
                ", status='" + status + '\'' +
                ", subTasks: [";
        boolean isFirstSubTask = true;
        for (Integer subTaskId : subTasks) {
            if (!isFirstSubTask) {
                result += ", ";
            } else {
                isFirstSubTask = false;
            }
            result += "id=" + subTaskId;
        }
        result += "]}";
        return result;
 */

}
