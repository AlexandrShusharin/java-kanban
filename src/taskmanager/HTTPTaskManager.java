package taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.KVTaskClient;
import task.Epic;
import task.Subtask;
import task.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient;
    private Gson json;

    public HTTPTaskManager(String url) {
        super();
        this.taskClient = new KVTaskClient(url);
        json = getTaskGson();
        load();
    }

    @Override
    public void save() {
        taskClient.put("tasks", json.toJson(tasks.values()));
        taskClient.put("epics", json.toJson(epics.values()));
        taskClient.put("subtasks", json.toJson(subtasks.values()));
        taskClient.put("history", json.toJson(getHistory()));
    }

    @Override
    public void load() {
        ArrayList<Task> loadTasks = json.fromJson(taskClient.load("tasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        if (loadTasks != null) {
            for (Task task : loadTasks) {
                updateTask(task);
            }
        }
        ArrayList<Epic> loadEpics = json.fromJson(taskClient.load("epics"),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());

        if (loadEpics != null) {
            for (Epic epic : loadEpics) {
                updateEpic(epic);
            }
        }

        ArrayList<Subtask> loadSubtasks = json.fromJson(taskClient.load("subtasks"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        if (loadSubtasks != null) {
            for (Subtask subtask : loadSubtasks) {
                updateSubtask(subtask);
            }
        }
        ArrayList<Task> loadHistory = json.fromJson(taskClient.load("history"),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        if (loadHistory != null) {
            for (Task task : loadHistory) {
                historyManager.add(task);
            }
        }
        setStartTaskId();
    }

   private void setStartTaskId() {
        List<Integer> allId = new ArrayList<>();
        allId.addAll(tasks.keySet());
        allId.addAll(subtasks.keySet());
        allId.addAll(epics.keySet());
        if (allId.size() > 0) {
            setLsatTaskId(Collections.max(allId));
        }
    }

    public static Gson getTaskGson () {
        return (new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create());
    }
}
