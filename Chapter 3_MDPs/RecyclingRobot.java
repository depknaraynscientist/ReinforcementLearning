import net.jcip.annotations.NotThreadSafe;

import javax.swing.plaf.nimbus.State;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@NotThreadSafe
public class RecyclingRobot {
    private final HashMap<String, StateClass> statesMap;
    static final float alpha = (float) 0.3; //prob of battery level from high to low
    static final float beta = (float) 0.7;  //prob of battery level from low to deplete
    static final double rewardSearch = 3; //expected no of cans the robot will collect while searching
    static final double rewardWait = 0.2; //expected no of cans someone drops cans in robot while waiting
    static final double rewardDeplete = -3; //when battery runs out
    static final float gamma = (float) 0.9;
    private double immediateReward;
    private StateClass currentState;
    private final StateClass high;
    private final StateClass low;
    private final ActionClass search, wait, recharge;

    RecyclingRobot(){
        statesMap = new HashMap<>();
        //create states
        high = new StateClass("high");
        low = new StateClass("low");
        //create actions
        search = new ActionClass("search");
        wait = new ActionClass("wait");
        recharge = new ActionClass("recharge");
        //add Actions to states
        high.addAction(search);high.addAction(wait);
        low.addAction(search);low.addAction(wait);low.addAction(recharge);
        //add states to robot
        statesMap.put("high", high);
        statesMap.put("low", low);

        currentState = statesMap.get("high"); //starting with state as high.
    }

    public void performAction(ActionClass a){
        if (a.equals(search)){
            System.out.println("Action equals search.");
            Search();
        }
        else if (a.equals(wait)){
            System.out.println("Action equals wait.");
            robotWait();
        }
        else if(a.equals(recharge)){
            System.out.println("Action equals recharge.");
            robotRecharge();
        }
        else{
            System.err.println("Should not have come into this else block since only 3 actions.");
        }
    }

    public void Search(){
        immediateReward = rewardSearch;
        int randomNo = ThreadLocalRandom.current().nextInt(1,101);
        System.out.println("Random Number generated for search : " + randomNo);
        if (currentState==high){
            if (randomNo > (alpha*100)){//if its >alpha, we change from high to low
                currentState = low;
            }
        }
        else{//if its in low state
            if (randomNo > (beta*100)){// if its greater than beta, then it depletes and changes to high
                immediateReward = rewardDeplete; //negative reward
                currentState = high;
            }
        }
    }

    public void robotWait(){
        immediateReward = rewardWait;
    }

    public void robotRecharge(){
        immediateReward = 0;
        currentState = high;
    }


    public double calculateValueFunction(StateClass s){
        double currentVal = s.getValue();
        double result = immediateReward + gamma * currentVal; // r + gamma*P*v
        return result;
    }

    public void runRobot(int noOfIterations){
        for (int i= 0; i<noOfIterations; i++){

        }
        if (currentState.equals(high)){

        }
        else{

        }

    }

    public static void main(String[] args) {
        RecyclingRobot obj1 = new RecyclingRobot();
        obj1.runRobot(1000);
    }

}
