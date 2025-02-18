package tasks;

import enums.Type;

import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId;

    public Epic(Type type, String name, String description, List<Integer> subtasksId) {
        super(type, name, description);
        this.subtasksId = subtasksId;
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