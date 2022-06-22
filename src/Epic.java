import java.util.Collection;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subTasks;

    public Epic(int id, String name, String description, String status) {
        super(id, name, description, status);
        subTasks = new ArrayList<>();
    }
    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask.getId());
    }
    public void removeSubTask(int id) {
        subTasks.remove(id);
    }
    public void removeAllSubTask() {
        subTasks.clear();
    }
    public ArrayList<Integer> getAllSubTask() {
        return subTasks;
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
        for (Integer subTaskId : subTasks) {
            if (!isFirstSubTask) {
                result += ", ";
            }
            else {
                isFirstSubTask = false;
            }
            result += "id=" + subTaskId;
        }
        result += "]}";
        return result;
    }
}
