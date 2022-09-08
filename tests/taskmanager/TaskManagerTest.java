package taskmanager;

import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Task task3;

    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;

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

    @Test
    void checkEpicStatus () {
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.NEW, epic1.getStatus(), "Неверный статус эпика (должен быть NEW");
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask1);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(),
                "Неверный статус эпика (должен быть IN_PROGRESS");

        subtask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);
        assertEquals(TaskStatus.DONE, epic1.getStatus(),
                "Неверный статус эпика (должен быть DONE");
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2);
        assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(),
                "Неверный статус эпика (должен быть IN_PROGRESS");
    }

       @Test
        void addEpic() {
            taskManager.addEpic(epic1);
            final Epic savedEpic = taskManager.getEpic(epic1.getId());

            assertNotNull(savedEpic, "Эпик не найден.");
            assertEquals(epic1, savedEpic, "Эпики не совпадают.");

            final List<Epic> epics = taskManager.getAllEpic();

            assertNotNull(epics, "Эпики  на возвращаются.");
            assertEquals(1, epics.size(), "Неверное количество 'эпиков'.");
            assertEquals(epic1, epics.get(0), "Эпики не совпадают.");
        }

        @Test
        void addTask(){
            taskManager.addTask(task1);
            final Task savedTask = taskManager.getTask(task1.getId());

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task1, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = taskManager.getAllTask();

            assertNotNull(tasks, "Задачи на возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        }

        @Test
        void addSubtask(){
            taskManager.addEpic(epic1);
            subtask1.setEpicId(epic1.getId());
            taskManager.addSubtask(subtask1);

            final Subtask savedSubtask = taskManager.getSubtask(subtask1.getId());

            assertNotNull(savedSubtask, "Подзадача не найдена.");
            assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают.");

            final List<Subtask> subtasks = taskManager.getAllSubtask();

            assertNotNull(subtasks, "Подзадачи на возвращаются.");
            assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
            assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают.");
        }

    @Test
    void updateEpic(){
        taskManager.addEpic(epic1);
        epic1.setDescription("тест");
        taskManager.updateEpic(epic1);
        assertNotNull(taskManager.getEpic(epic1.getId()), "Задача не найдена.");
        assertEquals(taskManager.getEpic(epic1.getId()).getDescription(), "тест", "Задача не обновляется");
    }

    @Test
    void updateTask(){
        taskManager.addTask(task1);
        task1.setDescription("тест");
        taskManager.updateTask(task1);
        assertNotNull(taskManager.getTask(task1.getId()), "Задача не найдена.");
        assertEquals(taskManager.getTask(task1.getId()).getDescription(), "тест", "Задача не обновляется");
    }

    @Test
    void updateSubtask(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        subtask1.setDescription("тест");
        taskManager.updateSubtask(subtask1);
        assertNotNull(taskManager.getSubtask(subtask1.getId()), "Подзадача не найдена.");
        assertEquals(taskManager.getSubtask(subtask1.getId()).getDescription(),
                "тест", "Подзадача не обновляется");
    }

    @Test
    void removeEpic(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeEpic(epic1.getId());
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Список эпиков не возвращается.");
        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(0, epics.size(), "Эпик не удалилися");
        assertEquals(0, subtasks.size(), "Подзадачи не удалились");
    }

    @Test
    void removeTask(){
        taskManager.addTask(task1);
        taskManager.removeTask(task1.getId());
        final List<Task> tasks = taskManager.getAllTask();
        assertNotNull(tasks, "Список задач не возвращается.");
        assertEquals(0, tasks.size(), "Задача не удалилилась");
    }

    @Test
    void removeSubtask(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        taskManager.removeAllSubtask();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(0, subtasks.size(), "Подзадача не удалилилась");
    }

    @Test
    void getEpic(){
        taskManager.addEpic(epic1);
        final Epic savedEpic = taskManager.getEpic(epic1.getId());

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic1, savedEpic, "Эпики не совпадают.");
    }

    @Test
    void getTask(){
        taskManager.addTask(task1);
        final Task savedTask = taskManager.getTask(task1.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задача не совпадают.");
    }

    @Test
    void getSubtask(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);

        final Subtask savedSubtask = taskManager.getSubtask(subtask1.getId());

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask1, savedSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void getAllEpic(){
        taskManager.addEpic(epic1);
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Список эпиков не возвращается.");
        assertEquals(1, epics.size(), "Не верное колличество эпиков");
    }

    @Test
    void getAllSubtask(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        final List<Subtask> tasks = taskManager.getAllSubtask();
        assertNotNull(tasks, "Список подзадач не возвращается.");
        assertEquals(1, tasks.size(), "Не верное колличество подзадач");
    }

    @Test
    void getAllTask(){
        taskManager.addTask(task1);
        final List<Task> tasks = taskManager.getAllTask();
        assertNotNull(tasks, "Список задач не возвращается.");
        assertEquals(1, tasks.size(), "Не верное колличество задач");
    }

    @Test
    void getEpicSubtasks(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        List<Subtask> subtasks = taskManager.getEpicSubtasks(epic1.getId());
        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(2, subtasks.size(), "Не верное колличество подзадач");
    }

    @Test
    void removeAllEpic(){
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeAllEpic();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        final List<Epic> epics = taskManager.getAllEpic();
        assertNotNull(epics, "Список эпиков не возвращается.");
        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(0, epics.size(), "Эпики не удалились");
        assertEquals(0, subtasks.size(), "Подзадачи не удалились");
    }

    @Test
    void removeAllTask(){
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.removeAllTask();
        final List<Task> tasks = taskManager.getAllTask();
        assertNotNull(tasks, "Список задач не возвращается.");
        assertEquals(0, tasks.size(), "Задачи не удалились");
    }

    @Test
    void removeAllSubtask(){
        taskManager.addEpic(epic1);
        subtask1.setEpicId(epic1.getId());
        subtask2.setEpicId(epic1.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.removeAllSubtask();
        final List<Subtask> subtasks = taskManager.getAllSubtask();
        assertNotNull(subtasks, "Список подзадач не возвращается.");
        assertEquals(0, subtasks.size(), "Подзадачи не удалились");
    }

    @Test
    void getHistory() {
        List<Task> history;

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());

        history = taskManager.getHistory();
        assertNotNull(history, "Список истории не возвращается");
        assertEquals(3, history.size(), "Неверное количество задач в истории");

        taskManager.getTask(task1.getId());
        history = taskManager.getHistory();
        assertEquals(task1.getId(), history.get(2).getId(), "Неверное очередность в истории");

        taskManager.removeTask(task1.getId());
        history = taskManager.getHistory();
        assertEquals(task3.getId(), history.get(1).getId(), "Неверное удаление задачи из истории (конец)");

        taskManager.addTask(task1);
        taskManager.getTask(task1.getId());
        taskManager.removeTask(task2.getId());
        history = taskManager.getHistory();
        assertEquals(task3.getId(), history.get(0).getId(), "Неверное удаление задачи из истории (начало)");

        taskManager.addTask(task2);
        taskManager.getTask(task2.getId());
        taskManager.removeTask(task1.getId());
        history = taskManager.getHistory();
        assertEquals(task2.getId(), history.get(1).getId(), "Неверное удаление задачи из истории (серидина)");

        taskManager.removeTask(task2.getId());
        taskManager.removeTask(task3.getId());
        history = taskManager.getHistory();
        assertNotNull(history, "Список истории не возвращается (после удаления)");
        assertEquals(0, history.size(), "История не очищается");
      }

    @Test
    void getPrioritizedTasks() {
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        final List<Task> tasks = taskManager.getPrioritizedTasks();
        assertNotNull(tasks, "Список отсортированных задач не возвращается.");
        assertEquals(3, tasks.size(), "Неверное колличество задач.");
        assertEquals(task1, tasks.get(2), "Неверная очередность задач");
    }
}
