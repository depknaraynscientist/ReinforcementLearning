import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class ActionClass{
    private final String actionName;
    @GuardedBy("this") private float actionValue;
    @GuardedBy("this") private int actionCount;
    @GuardedBy("this") private float trueActionValue;

    ActionClass(String name){
        synchronized (this){
            actionName = name;
            actionValue = 0;
            actionCount = 0;
            trueActionValue = 0;
        }
    }

    public synchronized float getTrueActionValue(){
        return trueActionValue;
    }

    public synchronized void setTrueActionValue(float inp){
        trueActionValue = inp;
    }

    public String getActionName(){
        return actionName;
    }

    public synchronized float updateActionValue(float reward){
        if (actionCount==0)
            throw new RuntimeException("ActionCount cannot be zero during update action value");
        actionValue = actionValue + ((reward - actionValue)/actionCount);
        return actionValue;
    }

    public synchronized float getActionValue(){
        return actionValue;
    }

    public synchronized int updateActionCount(){
        actionCount++;
        return actionCount;
    }

    public synchronized int getActionCount(){
        return actionCount;
    }
}
