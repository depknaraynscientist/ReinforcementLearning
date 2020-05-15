import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

@ThreadSafe
public class BanditAlgorithm {

    private final ConcurrentHashMap<Integer, ActionClass> actionsMap;
    private final PriorityQueue<ActionClass> greedyQueue;
    //comparator : descending order of action values
    private final Comparator<ActionClass> greedyQueueComparator = new Comparator<ActionClass>() {
        @Override
        public int compare(ActionClass o1, ActionClass o2) {
            float val1 = o1.getActionValue();
            float val2 = o2.getActionValue();
            return val1>=val2?-1:1;
        }
    };

    BanditAlgorithm(){
        actionsMap = new ConcurrentHashMap<>();
        greedyQueue = new PriorityQueue<>(greedyQueueComparator);
    }

public void performBanditAlgorithm(ConcurrentLinkedQueue<ActionClass> inpActions, int numberOfIterationsToRun, float probEpsilon){
    int qSize = inpActions.size();
    if (qSize==0)
        throw new RuntimeException("Number of Input Actions should be > 0.");
    //initializing
    System.out.println("Value of epsilon is : " + probEpsilon);
    System.out.println("----------------------------------------");
    var action = inpActions.poll();
    int count = 0;
    while (action !=null){//init
        //generateAndSetTrueActionValues(action); //sets q*(a)
        actionsMap.put(count, action);
        synchronized (this){
            greedyQueue.offer(action); //greedy action at top of Q. we need to remove and reinsert when value of elements change.
        }
        action = inpActions.poll();
        count++;
    }
    for (int i=0;i<numberOfIterationsToRun;i++){
        //Assuming that ThreadLocalRandom generates numbers in equal probability.
        //1. Generate random no btw 1 and 100(both inclusive) - will work for epsilon >=0.01 and <=0.99
        int randomNo = ThreadLocalRandom.current().nextInt(1,101);
        //System.out.println("Random Number generated : " + randomNo);
        ActionClass actionToRun;
        //choose greedy action with prob 1-epsilon and choose randomly from all with prob epsilon
        if (randomNo <= (probEpsilon*100)){
            synchronized (actionsMap){
                //generates random no btw 0 and size-1(both inclusive)
                int randomActionIndex = ThreadLocalRandom.current().nextInt(0,actionsMap.size());
                actionToRun = actionsMap.get(randomActionIndex);
            }
        }
        else{//choose greedyAction
            actionToRun = greedyQueue.peek(); //gets the top of greedy queue
        }
        synchronized (actionToRun){
            //System.out.println("Action Selected :" + actionToRun.getActionName());
            assert (greedyQueue.remove(actionToRun));  //assert that remove is true
            float rewardObtained = getReward(actionToRun);
            actionToRun.updateActionCount();
            actionToRun.updateActionValue(rewardObtained);
            greedyQueue.offer(actionToRun);
        }
    }
    for (var finalAction : actionsMap.values()){
        System.out.println("True Action value for action " +
                finalAction.getActionName() + " : " + finalAction.getTrueActionValue());
        System.out.println("Final Action value for action " +
                finalAction.getActionName() + " : " + finalAction.getActionValue());
    }
}

    public void clear(){
        synchronized (this){
            actionsMap.clear();
            greedyQueue.clear();
        }
    }

    //reward is from a gaussian with mean as trueActionValue and sd as 1.
    public float getReward(ActionClass a){
        Double rewardReturn = ThreadLocalRandom.current().nextGaussian() + a.getTrueActionValue();
        //System.out.println("Reward generated for action " + a.getActionName() + " : " + rewardReturn.floatValue());
        return rewardReturn.floatValue();
    }

    //resets the entire actionMap
    public ConcurrentHashMap<Integer, ActionClass> actionValuesReset(){
        synchronized (actionsMap){
            ConcurrentHashMap<Integer, ActionClass> temp = new ConcurrentHashMap<>();
            ActionClass newAction;
            for (var entry : actionsMap.entrySet()){
                var key = entry.getKey();
                var value = entry.getValue();
                newAction = new ActionClass(value.getActionName());
                newAction.setTrueActionValue(value.getTrueActionValue());
                temp.put(key, newAction);
            }
            clear();    //clears action map and greedy queue
            return temp;
        }
    }

    //below method is not being used as main method is currently setting it for us.
    //get action values from a gaussian distribution with 0 mean and unit variance.
    public void generateAndSetTrueActionValues(ActionClass a){
        Double val = ThreadLocalRandom.current().nextGaussian();
//        System.out.println("True Value generated from 0 mean 1 sd gaussian for action : "
//                            + a.getActionName() + " " + val.floatValue());
        a.setTrueActionValue(val.floatValue());
    }

}
