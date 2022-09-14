package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;
import taskmanager.HTTPTaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    protected KVServer kvServer;
    protected HttpTaskServer taskServer;
    protected HttpClient client;
    protected Gson gson;


    protected Task task1;
    protected Task task2;
    protected Task task3;

    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;


    @BeforeEach
    void setUp() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskServer = new HttpTaskServer();
        taskServer.start();
        client = HttpClient.newHttpClient();
        gson = HTTPTaskManager.getTaskGson();
        initTasks();
    }
    @Test
    void endPointTaskTest () throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        json = getTask(url).body();
        Task task = gson.fromJson(json, Task.class);
        assertEquals(task.getDescription(), task1.getDescription(), "Вернулась неверная задача");

        url = URI.create("http://localhost:8080/tasks/task/");
        json = getTask(url).body();
        ArrayList<Task> tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
        assertEquals(tasks.size(), 1, "Вернулоссь неверное колличество задач");

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        assertEquals(removeTask(url).statusCode(),200,"Вернулся код отличный от 200");
    }

    @Test
    void endPointEpicTest () throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        json = getTask(url).body();
        Epic epic = gson.fromJson(json, Epic.class);
        assertEquals(epic.getDescription(), epic1.getDescription(), "Вернулась неверная задача");

        url = URI.create("http://localhost:8080/tasks/epic/");
        json = getTask(url).body();
        ArrayList<Epic> tasks = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
        assertEquals(tasks.size(), 1, "Вернулоссь неверное колличество задач");

        url = URI.create("http://localhost:8080/tasks/epic/?id=1");
        assertEquals(removeTask(url).statusCode(),200,"Вернулся код отличный от 200");
    }

    @Test
    void endPointSubtaskTest () throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic1);
        postTask(json, url);
        subtask1.setEpicId(1);

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = gson.toJson(subtask1);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        json = getTask(url).body();
        Subtask subtask = gson.fromJson(json, Subtask.class);
        assertEquals(subtask.getDescription(), subtask1.getDescription(), "Вернулась неверная задача");

        url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=1");
        json = getTask(url).body();
        ArrayList<Subtask> tasks = gson.fromJson(json, new TypeToken<ArrayList<Subtask>>(){}.getType());
        assertEquals(tasks.size(), 1, "Вернулоссь неверное колличество подзадач эпика");

        url = URI.create("http://localhost:8080/tasks/subtask/");
        json = getTask(url).body();
        tasks = gson.fromJson(json, new TypeToken<ArrayList<Epic>>(){}.getType());
        assertEquals(tasks.size(), 1, "Вернулоссь неверное колличество подзадач");

        url = URI.create("http://localhost:8080/tasks/subtask/?id=2");
        assertEquals(removeTask(url).statusCode(),200,"Вернулся код отличный от 200");
    }

    @Test
    void endPointHistoryTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        json = gson.toJson(task2);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        url = URI.create("http://localhost:8080/tasks/task/?id=1");
        json = getTask(url).body();
        Task tempTask1 = gson.fromJson(json, Task.class);
        assertEquals(tempTask1.getDescription(), task1.getDescription(), "Вернулась неверная задача");

        url = URI.create("http://localhost:8080/tasks/task/?id=2");
        json = getTask(url).body();
        Task tempTask2 = gson.fromJson(json, Task.class);
        assertEquals(tempTask2.getDescription(), task2.getDescription(), "Вернулась неверная задача");


        url = URI.create("http://localhost:8080/tasks/history/");
        json = getTask(url).body();
        ArrayList<Task> tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
        assertEquals(tasks.size(), 2, "Вернулся неверный размер истории");
    }

    @Test
    void endPointTasksTest() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task1);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        json = gson.toJson(task2);
        assertEquals(postTask(json, url).statusCode(),200,"Вернулся код отличный от 200");

        url = URI.create("http://localhost:8080/tasks/");
        json = getTask(url).body();
        ArrayList<Task> tasks = gson.fromJson(json, new TypeToken<ArrayList<Task>>(){}.getType());
        assertEquals(tasks.size(), 2, "Вернулся неверный размер списка задач");
    }

    HttpResponse<String> postTask(String json, URI url) throws IOException, InterruptedException {
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    HttpResponse<String> getTask(URI url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    HttpResponse<String> removeTask(URI url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    @AfterEach
    void tearDown() {
        kvServer.stop();
        taskServer.stop();
    }

    protected void initTasks() {
        task1 = new Task("Задача 1", "Постричь кусты", TaskStatus.NEW, 120,
                LocalDateTime.parse("12-58-08-03-2022", Task.getTimeFormater()));
        task2 = new Task("Задача 2", "Полить газон", TaskStatus.NEW, 100,
                LocalDateTime.parse("10-58-08-03-2022", Task.getTimeFormater()));
        task3 = new Task("Задача 3", "Прополоть грядки", TaskStatus.NEW, 80,
                LocalDateTime.parse("11-58-08-03-2022", Task.getTimeFormater()));
        epic1 = new Epic("Эпик 1", "Посадить цветы", TaskStatus.NEW);
        epic2 = new Epic("Эпик 2", "Пожарить шашлык", TaskStatus.NEW);
        subtask1 = new Subtask("Подзадача 1", "Купить семяна", TaskStatus.NEW, 20,
                LocalDateTime.parse("16-58-08-03-2022", Task.getTimeFormater()), epic1.getId());
        subtask2 = new Subtask("Подзадача 2", "Вскопать гядку", TaskStatus.NEW, 15,
                LocalDateTime.parse("15-58-08-03-2022", Task.getTimeFormater()), epic1.getId());
    }
}