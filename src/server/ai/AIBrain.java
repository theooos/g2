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
     *
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
     * Arranges behaviours into sequences and performs any other bespoke behaviour
     * configuration.
     */
    protected abstract void configureBehaviours();

    /**
     * Calls upon the brain to evaluate the current situation and make the parent entity
     * carry out an appropriate action.
     */
    public abstract void doSomething();

    /**
     * Returns the requested behaviour from the brain's behaviour set.
     *
     * @param behaviour the name of the desired behaviour.
     * @return          the requested behaviour instance.
     */
    public Task getBehaviour(String behaviour){
        return behaviours.getBehaviour(behaviour);
    }

    /**
     * Calls upon the behaviour set to reset all progress and logic of all behaviours.
     */
    public void resetBehaviours(){
        behaviours.resetAll();
    }

    /**
     * Calls upon the brain to construct and configure its behaviours when all necessary
     * intelligence has been established.
     */
    public void equip(){
        constructBehaviours();
        configureBehaviours();
    }
}
