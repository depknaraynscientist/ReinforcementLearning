import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class runMainClass {
    public static void main(String[] args) {
        BanditAlgorithm obj1 = new BanditAlgorithm();
        //reference map
        ConcurrentHashMap<Integer, ActionClass> actions = new ConcurrentHashMap<>();
        for (int i=0; i< 10; i++){
            actions.put(i, new ActionClass("action " + i));
            obj1.generateAndSetTrueActionValues(actions.get(i));
        }
        int iterations = 10000;
        ConcurrentLinkedQueue<ActionClass> actionsQ = new ConcurrentLinkedQueue<>(actions.values());
        obj1.performBanditAlgorithm(actionsQ, iterations, (float) 0.01);
        obj1.clear();

        obj1.performBanditAlgorithm(actionsQ, iterations, (float) 0.1);
        obj1.clear();

        obj1.performBanditAlgorithm(actionsQ, iterations, (float) 0.3);
        obj1.clear();

        obj1.performBanditAlgorithm(actionsQ, iterations, (float) 0.9);
        obj1.clear();
    }


    //get action values from a gaussian distribution with 0 mean and unit variance.
    public void generateAndSetTrueActionValues(ActionClass a){
        Double val = ThreadLocalRandom.current().nextGaussian();
//        System.out.println("True Value generated from 0 mean 1 sd gaussian for action : "
//                            + a.getActionName() + " " + val.floatValue());
        a.setTrueActionValue(val.floatValue());
    }
}
