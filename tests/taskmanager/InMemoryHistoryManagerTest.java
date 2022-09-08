package taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {
    Task task;
    Task task2;
    Task task3;

    private HistoryManager historyManager;

    @BeforeEach
    public void beforeEach() {
        historyManager = new InMemoryHistoryManager();
        task = new Task("Задача 1", "Постричь кусты", TaskStatus.NEW, 120,
                LocalDateTime.parse("12-58-08-03-2022", Task.getTimeFormater()));
        task2 = new Task("Задача 2", "Полить газон", TaskStatus.NEW, 100);
    }


    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории не возвращается");
        assertEquals(1, history.size(), "История пустая, задача не добавилась.");
    }

    @Test
    void remove() {
        historyManager.add(task);
        historyManager.remove(task.getId());
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории не возвращается");
        assertEquals(0, history.size(), "История не пустая, задача не удалилась.");
    }

    @Test
    void getHistory() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "Список истории не возвращается");
        assertEquals(1, history.size(), "История задач пустая.");
    }
}