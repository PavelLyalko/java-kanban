package tasks;

import enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description, Status status, List<Integer> subtasksId) {
        super(name, description, status);
        this.subtasksId = subtasksId;
    }

    public List<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }
}