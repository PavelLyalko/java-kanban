package tasks;

import enums.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksId;
    private LocalDateTime endTime;


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

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

    @Override
    public LocalDateTime getEndTime() {
        endTime = getStartTime().plus(getDuration());
        return endTime;
    }

    @Override
    public String toString() {
        return super.toString() + "," + (endTime != null ? endTime.toString() : "");
    }
}