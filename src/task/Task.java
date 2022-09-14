package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected long duration;
    protected LocalDateTime startTime;


    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = 0;
        this.startTime = getUnsetDateTime();
    }
    public Task(String name, String description, TaskStatus status, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = getUnsetDateTime();
    }
    public Task(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public static DateTimeFormatter getTimeFormater() {
        return DateTimeFormatter.ofPattern("HH-mm-dd-MM-yyyy");
    }

    public static LocalDateTime getUnsetDateTime() {
        return LocalDateTime.parse("01-01-01-01-2200", getTimeFormater()).plusNanos((long)Math.random()*100000);
    }
    @Override
    public String toString() {
        return id +
                "," + TaskType.TASK +
                "," + name +
                "," + status +
                "," + description +
                "," + duration +
                "," + startTime.format(getTimeFormater()) +
                ",";
    }

}
