import Task.*;
import TaskManager.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();

        //Блок тестирования

        //создаем тестовые объекты
        Task task1 = new Task(taskManager.getNewTaskId(), "Задача 1", "Постричь кусты", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.getNewTaskId(), "Задача 2", "Полить газон", TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic(taskManager.getNewTaskId(), "Эпик 1", "Посадить цветы", TaskStatus.NEW);
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask(taskManager.getNewTaskId(), "Подзадача 1", "Купить семяна",
                TaskStatus.NEW, epic1);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(taskManager.getNewTaskId(), "Подзадача 2", "Вскопать гядку",
                TaskStatus.NEW, epic1);
        taskManager.addSubTask(subTask2);
        Epic epic2 = new Epic(taskManager.getNewTaskId(), "Эпик 2", "Пожарить шашлык", TaskStatus.NEW);
        taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask(taskManager.getNewTaskId(), "Подзадача 3", "Купить мясо",
                TaskStatus.NEW, epic2);
        taskManager.addSubTask(subTask3);
        SubTask subTask4 = new SubTask(taskManager.getNewTaskId(), "Подзадача 4", "Купить мясо",
                TaskStatus.NEW, epic2);
        taskManager.addSubTask(subTask4);
        SubTask subTask5 = new SubTask(taskManager.getNewTaskId(), "Подзадача 5", "Купить мясо",
                TaskStatus.NEW, epic2);
        taskManager.addSubTask(subTask5);

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println();

        //накрутка просмотров
        for (Task task : taskManager.getAllTask()) {
            taskManager.getTask(task.getId());
        }
        for (Task task : taskManager.getAllEpic()) {
            taskManager.getEpic(task.getId());
        }
        for (Task task : taskManager.getAllSubTask()) {
            taskManager.getSubTask(task.getId());
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
        SubTask subTaskUpd1 = taskManager.getSubTask(4);
        subTaskUpd1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(subTaskUpd1);
        taskManager.removeTask(2);
        taskManager.removeEpic(6);

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println();

        //очистка всех подзадач
        taskManager.removeAllSubTask();

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println();
    }
}
