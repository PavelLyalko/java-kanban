public class Main {
    public static void main(String[] args) {
        TaskManager taskmanager = new TaskManager();

        Task task1 = new Task();
        task1.setName("task1Name");
        task1.setDescription("task1Description");

        Task task2 = new Task();
        task2.setName("task2Name");
        task2.setDescription("task2Description");

        Epic epic1 = new Epic();
        epic1.setName("epic1Name");
        epic1.setDescription("epic1Description");

        Subtask subtask1 = new Subtask();
        subtask1.setName("subtask1Name");
        subtask1.setDescription("subtask2Description");

        Subtask subtask2 = new Subtask();
        subtask2.setName("subtask2Name");
        subtask2.setDescription("subtask2Description");

        Epic epic2 = new Epic();
        epic2.setName("epic2Name");
        epic2.setDescription("epic2Description");

        Subtask subtask3 = new Subtask();
        subtask3.setName("subtask3Name");
        subtask3.setDescription("subtask3Description");

        taskmanager.add(task1);
        taskmanager.add(task2);

        taskmanager.add(epic1);
        taskmanager.add(subtask1,3);
        taskmanager.add(subtask2,3);

        taskmanager.add(epic2);
        taskmanager.add(subtask3, 6);

        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());

        taskmanager.update(task1,Status.IN_PROGRESS);
        taskmanager.update(subtask1,Status.IN_PROGRESS);
        taskmanager.update(epic2,Status.IN_PROGRESS);

        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());

        taskmanager.deleteTask(1);
        taskmanager.deleteEpic(6);
        taskmanager.deleteSubtask(4);

        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());


    }
}

