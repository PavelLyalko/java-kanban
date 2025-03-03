package tasks;

import enums.Type;

import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(Type type, String name, String description, int epicId, int duration, LocalDateTime startTime) {
        super(type, name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, Type type, String name, String description, int epicId, int duration, LocalDateTime startTime) {
        super(id, type, name, description, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return super.toString() + "," + epicId;
    }

}