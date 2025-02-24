package manager;

import enums.Type;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager loadFromFile(File file) throws FileNotFoundException {
        int count = 0;
        if (file.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                bufferedReader.skip(38);
                while (bufferedReader.ready()) {
                    Task task = fromString(bufferedReader.readLine());
                    if (task == null) throw new IOException();
                    if (task instanceof Subtask) {
                        getSubtasksMap().put(task.getId(), (Subtask) task);
                    } else if (task instanceof Epic) {
                        getEpicsMap().put(task.getId(), (Epic) task);
                    } else {
                        getTasksMap().put(task.getId(), task);
                    }
                    count++;
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении из файла.");
            }
        } else {
            throw new FileNotFoundException("Файл не найден!");
        }
        this.file = file;
        setNextId(count);
        return new FileBackedTaskManager();
    }

    private void save() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
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

    private static Task fromString(String value) {
        String[] task = value.split(",");

        if (task[1].equals(Type.TASK.toString())) {
            return new Task(Integer.parseInt(task[0]), Type.TASK, task[2], task[4]);
        } else if (task[1].equals(Type.EPIC.toString())) {
            return new Epic(Integer.parseInt(task[0]), Type.EPIC, task[2], task[4]);
        } else if (task[1].equals(Type.SUBTASK.toString())) {
            return new Subtask(Integer.parseInt(task[0]), Type.SUBTASK, task[2], task[4], Integer.parseInt(task[5]));
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

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }
}
