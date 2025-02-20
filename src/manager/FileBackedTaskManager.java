package manager;

import enums.Type;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.Loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager loadFromFile(File file) {
        if (file.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                bufferedReader.skip(38);
                while (bufferedReader.ready()) {
                    add(fromString(bufferedReader.readLine()));
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении из файла.");
            }
        } else {
            System.out.println("Файл не найден!");

            return null;
        }
        return new FileBackedTaskManager();
    }

    public void save() {
        Loader.loadPropertiesToMap().get("filename");

        try (BufferedWriter br = new BufferedWriter(new FileWriter("test"))) {
            br.write("id,type,name,status,description,epic \n");
            for (Task task : getTasks()) {
                br.write(task.toString() + ",\n");
            }

            for (Epic epic : getEpics()) {
                br.write(epic.toString() + ",\n");
            }

            for (Subtask subtask : getSubtasks()) {
                br.write(subtask.toString() + ",\n");
            }

        } catch (IOException e) {
            System.out.println("Произошла ошибка при записи в файл.");
        }
    }

    public Task fromString(String value) {
        String[] task = value.split(",");

        if (task[1].equals(Type.TASK.toString())) {
            return new Task(Type.TASK, task[2], task[4]);
        } else if (task[1].equals(Type.EPIC.toString())) {
            return new Epic(Type.EPIC, task[2], task[4], new ArrayList<>());
        } else if (task[1].equals(Type.SUBTASK.toString())){
            return new Subtask(Type.SUBTASK, task[2], task[4], Integer.parseInt(task[5]));
        }
        return null;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }
}
