package server.ai;

import server.ai.decision.Check;
import server.ai.behaviour.*;
import server.ai.decision.Feel;

/**
 * Abstract representation of an AI-controlled unit's brain.
 *
 * Created by Rhys on 3/8/17.
 */
public abstract class AIBrain {

    protected Intel intel;
    protected Check check;
    protected BehaviourSet behaviours;

    /**
     * Initialises generic members.
     * @param intel the intelligence object the brain will use to make decisions.
     */
    public AIBrain(Intel intel) {
        this.intel = intel;
        this.check = new Check(intel);
        this.behaviours = new BehaviourSet();
    }

    /**
     * Constructs entity-generic behaviours and adds them to the behaviour set.
     */
    protected void constructBehaviours(){
        behaviours.addBehaviour(new Dawdle(intel, this), "Dawdle");
        behaviours.addBehaviour(new FindPath(intel, this), "FindPath");
        behaviours.addBehaviour(new LocateCover(intel, this), "LocateCover");
        behaviours.addBehaviour(new Wander(intel, this), "Wander");
        behaviours.addBehaviour(new Zap(intel, this), "Zap");
    }

    /**
     * Arranges behaviours into
     */
    protected abstract void configureBehaviours();

    public abstract void doSomething();

    public Task getBehaviour(String behaviour){
        return behaviours.getBehaviour(behaviour);
    }

    public void resetBehaviours(){
        behaviours.resetAll();
    }

    public void equip(){
        constructBehaviours();
        configureBehaviours();
    }
}
