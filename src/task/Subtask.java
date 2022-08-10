package task;

public class Subtask extends Task {
    public int epicId;

    public Subtask(String name, String description, TaskStatus status, Epic epic) {
        super(name, description, status);
        this.epicId = epic.id;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return  id +
                "," + TaskType.SUBTASK +
                "," + name +
                "," + status +
                "," + description +
                "," + epicId;
    }
//старая реализация
/*        return "Task.SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.lenght='" + description.length() + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
 */
}
