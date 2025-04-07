package tasks;

import enums.Status;
import enums.Type;
import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.Objects;

public class Task {
    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private Status status;

    @SerializedName("id")
    private Integer id;

    @SerializedName("type")
    private Type type;

    @SerializedName("duration")
    private Duration duration;

    @SerializedName("startTime")
    private LocalDateTime startTime;

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Task() {

    }

    public Task(Type type, String name, String description, int duration, LocalDateTime startTime) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(Type type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(int id, Type type, String name, String description, int duration, LocalDateTime startTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(int id, Type type, String name, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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

    public Integer getId() {
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
        return id +
                "," + type +
                "," + name +
                "," + status +
                "," + description +
                "," + (startTime != null ? startTime.toString() : "") +
                "," + (duration != null ? duration.toMinutes() : "");
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