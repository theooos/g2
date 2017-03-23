package server.ai.behaviour;

import server.ai.Task;

import java.util.HashMap;

/**
 * Acts as a container for all behaviours available to a particular brain, so
 * that single instances can be shared by multiple emotional processes and reset
 * en-masse.
 *
 * Created by Rhys on 3/8/17.
 */
public class BehaviourSet {

    private HashMap<String, Task> behaviours;

    /**
     * Constructs an empty behaviour set.
     */
    public BehaviourSet(){
        this.behaviours = new HashMap<>();
    }

    /**
     * Adds a behaviour to the set.
     *
     * @param t the behaviour instance to be added to the set.
     * @param n a string representing the name of the behaviour.
     */
    public void addBehaviour(Task t, String n){
        behaviours.put(n, t);
    }

    /**
     * Fetches a behaviour from the set whose key matches the given string.
     *
     * @param n the name of the desired behaviour.
     * @return  the desired behaviour.
     */
    public Task getBehaviour(String n){
        return behaviours.get(n);
    }

    /**
     * Resets progress and logic of all behaviours stored within the set so
     * that they are fresh for re-use by a new emotional process.
     */
    public void resetAll(){
        for (java.util.Map.Entry e : behaviours.entrySet()) {
            Task toReset = (Task) e.getValue();
            toReset.reset();
        }
    }
}
