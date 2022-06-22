public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        //Блок тестирования

        //создаем тестовые объекты
        Task task1 = new Task(taskManager.getNewTaskId(), "Задача 1", "Постричь кусты", "NEW");
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.getNewTaskId(), "Задача 2", "Полить газон", "NEW");
        taskManager.addTask(task2);
        Epic epic1 = new Epic(taskManager.getNewTaskId(), "Эпик 1", "Посадить цветы", "NEW");
        taskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask(taskManager.getNewTaskId(), "Подзадача 1", "Купить семяна",
                "NEW", epic1);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(taskManager.getNewTaskId(), "Подзадача 2", "Вскопать гядку",
                "NEW", epic1);
        taskManager.addSubTask(subTask2);
        Epic epic2 = new Epic(taskManager.getNewTaskId(), "Эпик 2", "Пожарить шашлык", "NEW");
        taskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask(taskManager.getNewTaskId(), "Подзадача 3", "Купить мясо",
                "NEW", epic2);
        taskManager.addSubTask(subTask3);

        //вывод результата
        System.out.println(taskManager.getAllTask());
        System.out.println(taskManager.getAllEpic());
        System.out.println(taskManager.getAllSubTask());
        System.out.println();

        //меняем объекты
        SubTask subTaskUpd1 = taskManager.getSubTask(4);
        subTaskUpd1.setStatus("DONE");
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
