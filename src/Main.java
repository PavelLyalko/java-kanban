import enums.Status;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Managers managers = new Managers();
        TaskManager taskmanager = managers.getDefault();

        Task task1 = new Task("task1Name","task1Description", Status.NEW );
        Task task2 = new Task("task2Name","task2Description",Status.NEW );

        Epic epic1 = new Epic("epic1Name","epic1Description", Status.NEW, new ArrayList<>() );
        Subtask subtask1 = new Subtask("subtask1NameEpic1Name", "subtask2Description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("subtask2NameEpic1Name","subtask2Description", Status.NEW, epic1.getId());

        Epic epic2 = new Epic("epic2Name", "epic2Description", Status.NEW, new ArrayList<>());
        Subtask subtask3 = new Subtask("subtask3NameEpic2Name", "subtask3Description", Status.NEW, epic2.getId());

        taskmanager.add(task1);
        taskmanager.add(task2);

        taskmanager.add(epic1);
        taskmanager.add(subtask1);
        taskmanager.add(subtask2);

        taskmanager.add(epic2);
        taskmanager.add(subtask3);

        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());

        task1.setStatus(Status.IN_PROGRESS);
        taskmanager.update(task1);

        task2.setStatus(Status.DONE);
        taskmanager.update(task2);

        subtask1.setStatus(Status.IN_PROGRESS);
        taskmanager.update(subtask1);

        epic1.setStatus(Status.DONE);
        taskmanager.update(epic1);


        System.out.println();
        System.out.println("_________________________");
        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());
        System.out.println("_________________________");
        System.out.println();

        taskmanager.deleteTask(1);
        taskmanager.deleteEpic(6);
        taskmanager.deleteSubtask(4);

        System.out.println();
        System.out.println("_________________________");
        System.out.println(taskmanager.getTasks());
        System.out.println(taskmanager.getEpics());
        System.out.println(taskmanager.getSubtasks());
        System.out.println("_________________________");
        System.out.println();
    }
}