import net.jcip.annotations.NotThreadSafe;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

@NotThreadSafe
public class StateClass {
    private final String name;
    private final LinkedList<ActionClass> actions = new LinkedList<>();
    private double value;
    private HashMap<ActionClass, Double> actionValues = new HashMap<>(); //stores values for each action
    private PriorityQueue<ActionClass> actionsValues = new PriorityQueue<>();


    StateClass(String inpName){
        name = inpName;
        value = 0;
    }

    public String getName(){
        return name;
    }

    public void updateActionValue(ActionClass a, double inpValue){
        actionValues.put(a, inpValue);
    }

    public double getActionValue(ActionClass a){
        return actionValues.get(a);
    }

    public void setValue(double inp){
        value = inp;
    }

    public double getValue(){
        return value;
    }

    public void addAction(ActionClass a){
        actions.add(a);
    }

    public boolean removeAction(ActionClass a){
        return actions.remove(a);
    }

    public LinkedList<ActionClass> getActions(){
        return actions;
    }

}
