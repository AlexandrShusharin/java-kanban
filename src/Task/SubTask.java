package Task;

public class SubTask extends Task {
    public int epicId;

    public SubTask(int id, String name, String description, TaskStatus status, Epic epic) {
        super(id, name, description, status);
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
        return "Task.SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description.lenght='" + description.length() + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }

}
