package tasks;

import enums.Status;
import enums.Type;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;
    private Type type;

    public Task(Type type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, Type type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;

    }

    @Override
    public String toString() {
        return id + '\'' +
                "," + type +
                "," + name +
                "," + status +
                "," + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Type getType() {
        return type;
    }

}