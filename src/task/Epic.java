package task;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTasks;
    protected LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
        subTasks = new ArrayList<>();
        endTime = this.startTime.plusMinutes(1);
    }

    public void addSubtask(Integer subtaskId) {
        subTasks.add(subtaskId);
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return  id +
                "," + TaskType.EPIC +
                "," + name +
                "," + status +
                "," + description +
                "," +
                "," +
                "," ;
    }
}
