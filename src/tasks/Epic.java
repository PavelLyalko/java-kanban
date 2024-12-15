package tasks;

import enums.Status;

import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId;

    public Epic(String name, String description, Status status, List<Integer> subtasksId) {
        super(name, description, status);
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