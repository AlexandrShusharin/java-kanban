import task.*;
import taskmanager.*;

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
        Epic epic2 = new Epic("Эпик 2", "Пожарить шашлык", TaskStatus.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Подзадача 3", "Купить мясо", TaskStatus.NEW, epic2);
        taskManager.addSubtask(subtask3);
        Subtask subtask4 = new Subtask("Подзадача 4", "Купить мясо", TaskStatus.NEW, epic2);
        taskManager.addSubtask(subtask4);
        Subtask subtask5 = new Subtask("Подзадача 5", "Купить мясо", TaskStatus.NEW, epic2);
        taskManager.addSubtask(subtask5);

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
        System.out.println();

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
        for (Task task : taskManager.getAllTask()) {
            taskManager.getTask(task.getId());
        }

        //проверка истории
        System.out.println("История просмотров: ");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("--------------Окончание истории-----------------");

        //меняем объекты (проверка изменения статуса эпика")
        Subtask subtaskUpd1 = taskManager.getSubtask(4);
        subtaskUpd1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskUpd1);
        taskManager.removeTask(2);
        taskManager.removeEpic(6);

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
        System.out.println();

        //очистка всех подзадач
        taskManager.removeAllSubtask();

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubtask());
        System.out.println();
    }
}
