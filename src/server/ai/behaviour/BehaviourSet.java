package server.ai.behaviour;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by rhys on 3/8/17.
 */
public class BehaviourSet {

    private HashMap<String, Task> behaviours;

    public BehaviourSet(){
        this.behaviours = new HashMap<>();
    }

    public void addBehaviour(Task t, String n){
        behaviours.put(n, t);
    }

    public Task getBehaviour(String n){
        return behaviours.get(n);
    }

    public void resetAll(){
        for (java.util.Map.Entry e : behaviours.entrySet()) {
            Task toReset = (Task) e.getValue();
            toReset.reset();
        }
    }
}
