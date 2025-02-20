package tasks;

import enums.Type;

public class Subtask extends Task {
    private int epicId;

    public Subtask(Type type, String name, String description, int epicId) {
        super(type, name, description);
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