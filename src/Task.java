import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private Status status;
    private int id;


    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public Status getStatus(){
        return status;
    }
    public int getId(){
        return id;
    }
    public void setStatus(Status status){
        this.status = status;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
