package manager;

import enums.Type;
import exceptions.TimeIntersectionException;
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
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static File file;

    private FileBackedTaskManager(File currentFile) {
        file = currentFile;
    }

    public static FileBackedTaskManager loadFromFile(File currentFile) throws FileNotFoundException {
        int count = 0;
        if (currentFile.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(currentFile))) {
                bufferedReader.skip(38);
                while (bufferedReader.ready()) {
                    Task task = fromString(bufferedReader.readLine());
                    if (task == null) throw new IOException();
                    if (task instanceof Subtask) {
                        subtasks.put(task.getId(), (Subtask) task);
                    } else if (task instanceof Epic) {
                        epics.put(task.getId(), (Epic) task);
                    } else {
                        tasks.put(task.getId(), task);
                    }
                    count++;
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка при чтении из файла.");
            }
        } else {
            throw new FileNotFoundException("Файл не найден!");
        }
        setNextId(count);
        return new FileBackedTaskManager(currentFile);
    }

    private void save() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            br.write("id,type,name,status,description,epic \n");

            Stream.concat(Stream.concat(getTasks().stream(), getEpics().stream()), getSubtasks().stream())
                    .map(Object::toString)
                    .forEach(taskString -> {
                        try {
                            br.write(taskString + ",\n");
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });

        } catch (IOException e) {
            System.out.println("Произошла ошибка при записи в файл.");
        }
    }

    private static Task fromString(String value) {
        String[] task = value.split(",");

        if (task[1].equals(Type.TASK.toString())) {
            return new Task(Integer.parseInt(task[0]), Type.TASK, task[2], task[4], Integer.parseInt(task[6]), LocalDateTime.parse(task[5]));
        } else if (task[1].equals(Type.EPIC.toString())) {
            return new Epic(Integer.parseInt(task[0]), Type.EPIC, task[2], task[4]);
        } else if (task[1].equals(Type.SUBTASK.toString())) {
            return new Subtask(Integer.parseInt(task[0]), Type.SUBTASK, task[2], task[4], Integer.parseInt(task[5]), Integer.parseInt(task[7]), LocalDateTime.parse(task[6]));
        }
        return null;
    }

    @Override
    public void add(Task task) throws TimeIntersectionException {
        super.add(task);
        save();
    }

    @Override
    public void add(Subtask subtask) throws TimeIntersectionException {
        super.add(subtask);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void update(Task task) throws TimeIntersectionException {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) throws TimeIntersectionException {
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
