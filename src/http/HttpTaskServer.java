package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import task.Epic;
import task.Subtask;
import task.Task;
import taskmanager.HTTPTaskManager;
import taskmanager.Managers;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final int PORT = 8080;
    private Gson json;
    private TaskManager taskManager;
    private HttpServer server;

    public HttpTaskServer() throws IOException {
        this.json = HTTPTaskManager.getTaskGson();
        this.taskManager = Managers.getDefault();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);

        server.createContext("/tasks", this::handlerAllTask);
        server.createContext("/tasks/task", this::handlerTask);
        server.createContext("/tasks/epic", this::handlerEpic);
        server.createContext("/tasks/subtask", this::handlerSubtask);
        server.createContext("/tasks/history", this::handlerHistory);
        server.createContext("/tasks/subtask/epic", this::handlerEpicSubtasks);
    }
    private void handlerAllTask(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/tasks getAll");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, json.toJson(new ArrayList<Task>(taskManager.getPrioritizedTasks())));
            } else {
                System.out.println("/tasks ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void handlerHistory(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/tasks History");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, json.toJson(new ArrayList<Task>(taskManager.getHistory())));
            } else {
                System.out.println("/tasks ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void handlerTask(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/Поступил запрос на task");
            String requestMethod = h.getRequestMethod();
            int taskId = getTaskIdFromPath(h.getRequestURI().getQuery());
            switch (requestMethod) {
                case "GET":
                    if (taskId > 0) {
                        sendText(h, json.toJson(taskManager.getTask(taskId)));
                    } else {
                        sendText(h, json.toJson(new ArrayList<Task>(taskManager.getAllTask())));
                    }
                    break;
                case "POST":
                    Task task = json.fromJson(readText(h), Task.class);
                    if (taskId > 0) {
                        taskManager.updateTask(task);
                    } else {
                        taskManager.addTask(task);
                    }
                    sendText(h, "");
                    break;
                case "DELETE":
                    if (taskId > 0) {
                        taskManager.removeTask(taskId);
                    } else {
                        taskManager.removeAllTask();
                    }
                    sendText(h, "");
                    break;
            }
        } finally {
            h.close();
        }
    }

    private void handlerEpic(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/Поступил запрос на Epic");
            String requestMethod = h.getRequestMethod();
            int taskId = getTaskIdFromPath(h.getRequestURI().getQuery());
            switch (requestMethod) {
                case "GET":
                    if (taskId > 0) {
                        sendText(h, json.toJson(taskManager.getEpic(taskId)));
                    } else {
                        sendText(h, json.toJson(new ArrayList<Epic>(taskManager.getAllEpic())));
                    }
                    break;
                case "POST":
                    Epic epic = json.fromJson(readText(h), Epic.class);
                    if (taskId > 0) {
                        taskManager.updateEpic(epic);
                    } else {
                        taskManager.addEpic(epic);
                    }
                    sendText(h, "");
                    break;
                case "DELETE":
                    if (taskId > 0) {
                        taskManager.removeEpic(taskId);
                    } else {
                        taskManager.removeAllEpic();
                    }
                    sendText(h, "");
                    break;
            }
        } finally {
            h.close();
        }
    }

    private void handlerSubtask(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/Поступил запрос на subtask");
            String requestMethod = h.getRequestMethod();
            int taskId = getTaskIdFromPath(h.getRequestURI().getQuery());
            switch (requestMethod) {
                case "GET":
                    if (taskId > 0) {
                        sendText(h, json.toJson(taskManager.getSubtask(taskId)));
                    } else {
                        sendText(h, json.toJson(new ArrayList<Subtask>(taskManager.getAllSubtask())));
                    }
                    break;
                case "POST":
                    Subtask subtask = json.fromJson(readText(h), Subtask.class);
                    if (taskId > 0) {
                        taskManager.updateSubtask(subtask);
                    } else {
                        taskManager.addSubtask(subtask);
                    }
                    sendText(h, "");
                    break;
                case "DELETE":
                    if (taskId > 0) {
                        taskManager.removeSubtask(taskId);
                    } else {
                        taskManager.removeAllSubtask();
                    }
                    sendText(h, "");
                    break;
            }
        } finally {
            h.close();
        }
    }

    private void handlerEpicSubtasks(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/Поступил запрос на subtask epic");
            String requestMethod = h.getRequestMethod();
            int taskId = getTaskIdFromPath(h.getRequestURI().getQuery());
            if (requestMethod.equals("GET")) {
                if (taskId > 0) {
                    sendText(h, json.toJson(new ArrayList<Subtask>(taskManager.getEpicSubtasks(taskId))));
                } else {
                    sendText(h, "");
                }
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем TaskManager сервер на порту " + PORT);
        server.start();
    }
    public void stop() {
        System.out.println("Серевер TaskManager остановлен" + PORT);
        server.stop(0);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private int getTaskIdFromPath (String query) {
        if (query == null) {
            return 0;
        }
        String[] pathPats = query.split("id=");
        System.out.println(pathPats.length);
        if (pathPats.length > 1) {
            return Integer.parseInt(pathPats[1]);
        }
        return 0;
    }
}
