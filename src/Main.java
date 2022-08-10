import task.*;
import taskmanager.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        //Блок тестирования

        //создаем тестовые объекты
        Task task1 = new Task("Задача 1", "Постричь кусты", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача 2", "Полить газон", TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Эпик 1", "Посадить цветы", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Купить семяна", TaskStatus.NEW, epic1);
        taskManager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Вскопать гядку", TaskStatus.NEW, epic1);
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Полить", TaskStatus.NEW, epic1);
        taskManager.addSubtask(subtask3);
        Epic epic2 = new Epic("Эпик 2", "Пожарить шашлык", TaskStatus.NEW);
        taskManager.addEpic(epic2);


        //вывод результата
        //System.out.println(taskManager.getAllTask());
        //System.out.println(taskManager.getAllEpic());
        //System.out.println(taskManager.getAllSubtask());


        //System.out.println();

        //накрутка просмотров
        for (Task task : taskManager.getAllTask()) {
            taskManager.getTask(task.getId());
        }
        for (Task task : taskManager.getAllEpic()) {
            taskManager.getEpic(task.getId());
        }
        for (Task task : taskManager.getAllSubtask()) {
            taskManager.getSubtask(task.getId());
        }
/*
        //проверка истории
        printHistory(taskManager.getHistory());

        //произвольный вызов тасков
        taskManager.getTask(2);
        taskManager.getTask(1);
        taskManager.getSubtask(6);
        taskManager.getEpic(3);

        //проверка истории
        printHistory(taskManager.getHistory());

        //удаление тасков
        taskManager.removeTask(2);
        taskManager.removeEpic(3);

        //проверка истории
        printHistory(taskManager.getHistory());

        //удаление тасков (дополнительная проверка на крайние значения)
        taskManager.removeEpic(7);
        taskManager.removeTask(1);

        //проверка истории
        printHistory(taskManager.getHistory());
    }
    private static void printHistory(List<Task> taskManager) {
        System.out.println();
        System.out.println("История просмотров: ");
        for (Task task : taskManager) {
            System.out.println(task);
        }
        System.out.println("--------------Окончание истории-----------------");
 */
     }
}
