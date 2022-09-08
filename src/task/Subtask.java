package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    public int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, long duration, int epicId) {
        super(name, description, status, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, long duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("HH-mm-dd-MM-yyyy");
        return  id +
                "," + TaskType.SUBTASK +
                "," + name +
                "," + status +
                "," + description +
                "," + duration +
                "," + startTime.format(getTimeFormater()) +
                "," + epicId;
    }
}
