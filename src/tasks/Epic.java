package tasks;

import enums.Type;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId;

    public Epic(Type type, String name, String description) {
        super(type, name, description);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(int id, Type type, String name, String description) {
        super(id, type, name, description);
        this.subtasksId = new ArrayList<>();
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(int id) {
        if (this.getId() != id) {
            subtasksId.add(id);
        }
    }
}