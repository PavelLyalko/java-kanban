import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public ArrayList<Integer> getSubtasksId(){
        return subtasksId;
    }
    public void addList(Integer id){
        subtasksId.add(id);
    }


}

