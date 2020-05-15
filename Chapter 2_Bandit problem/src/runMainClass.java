import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class runMainClass {
    public static void main(String[] args) {
        long start = System.nanoTime();
        BanditAlgorithm obj1 = new BanditAlgorithm();
        ConcurrentHashMap<Integer, ActionClass> action_obj = new ConcurrentHashMap<>();
        for (int i=0; i< 10; i++){
            action_obj.put(i, new ActionClass("action " + i));
            generateAndSetTrueActionValues(action_obj.get(i));
        }
        int iterations = 100000;
        action_obj = runBandit(obj1, action_obj, iterations, (float) 0.1);
        action_obj = runBandit(obj1, action_obj, iterations, (float) 0.3);
        action_obj = runBandit(obj1, action_obj, iterations, (float) 0.9);
        double execTime = (System.nanoTime()-start)/1e9;
        System.out.println(execTime);
    }


    public static ConcurrentHashMap<Integer, ActionClass> runBandit(BanditAlgorithm obj1, ConcurrentHashMap<Integer, ActionClass> actions, int iterations, float epsilon){
        ConcurrentLinkedQueue<ActionClass> actionsQ = new ConcurrentLinkedQueue<>(actions.values());
        obj1.performBanditAlgorithm(actionsQ, iterations, (float) epsilon);
        return obj1.actionValuesReset();
    }

    //get action values from a gaussian distribution with 0 mean and unit variance.
    public static void generateAndSetTrueActionValues(ActionClass a){
        Double val = ThreadLocalRandom.current().nextGaussian();
//        System.out.println("True Value generated from 0 mean 1 sd gaussian for action : "
//                            + a.getActionName() + " " + val.floatValue());
        a.setTrueActionValue(val.floatValue());
        System.out.println("Value of " + a.getActionName() + " : " + val.floatValue());
    }
}
