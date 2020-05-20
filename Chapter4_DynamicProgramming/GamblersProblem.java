import com.opencsv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static java.nio.file.Files.newBufferedWriter;

public class GamblersProblem {

    //key is index, value is value of state
    private final HashMap<Integer, Double> valueMap = new HashMap<>();
    //key is index, value is all values generated for all actions in the same state
    //so '1' is the value for the state when action = 1. similarly 2,3, etc..
    HashMap<Integer, Double> actionValuesMap = new HashMap<>();

    public void runGambler(double threshold, String fileNameEnd) throws IOException {
        double theta_threshold = threshold;
        double delta = 0.0;
        double pH = 0.4;
        double gamma = 0.9;
        int reward = 0;
        Writer writer = newBufferedWriter(Paths.get("./output" + fileNameEnd + ".csv"));
        CSVWriter csvWriter = new CSVWriter(writer);
        for (int i = 0; i < 101 ; i++){
            if (i!=100)
                valueMap.put(i, 0.0);
            else
                valueMap.put(i, 1.0);
        }
        for (int i = 0; i < 51 ; i++){
            actionValuesMap.put(i, 0.0);
        }
        int counter = 0;
        do{
            counter++;
            delta = 0.0;
            for (int i = 0; i < 100; i++){//looping through all states.
                double oldValue = valueMap.get(i); //gets the old value from the map.
                //get an action
                int action = Integer.min(i, 100-i); //taking the minimum of the above values.
                for (int a = 1; a <= action; a++){//loop thru all actions.
                    if (i + a == 100){//terminal state reached.
                        reward = 1;
                    }
                    else{
                        reward = 0;
                    }
                    //here, if we make a stake, 0.4 prob of  winning, reward=0(except for terminal state)
                    double newValue = pH * (0 + (gamma * valueMap.get(i + a)));
                    //0.6 prob of losing, reward=0 (-1 for terminal state)
                    newValue = newValue + (1-pH) * (0 + (gamma * valueMap.get(i - a)));
                    actionValuesMap.put(a, newValue);
                }
                double maxValue = 0;
                for (int j = 1; j <= action ; j++){
                    double temp = actionValuesMap.get(j);
                    if (temp > maxValue){
                        maxValue = temp;
                    }
                }
                valueMap.put(i, maxValue); //finally value map has the max value from all the actions from the state
                delta = Double.max(delta, Math.abs(maxValue-oldValue)); //updates delta to get delta from all states
            }
            //System.out.println("delta is : " + delta);
        }
        while (delta > theta_threshold);//if delta is greater, loop again. if delta is less, means that all values are less than the threshold bcos earlier we took max().
        for (var key : valueMap.keySet()){
            csvWriter.writeNext(new String[]{String.valueOf(key), valueMap.get(key).toString()});
            System.out.println("Capital : " + key + " Value Estimates : " + valueMap.get(key));
        }
        System.out.println(counter);
        csvWriter.close();
        writer.close();
    }
    
    public static void main(String[] args) throws IOException {
        GamblersProblem obj1 = new GamblersProblem();
        obj1.runGambler(0.1, "_1");
        obj1.runGambler(0.001 , "_2");
        obj1.runGambler(0.0000000000001, "_3");
        obj1.runGambler(0.00000000000000000000000000001, "_4");
    }
}
