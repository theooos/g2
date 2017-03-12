package server.ai;

import server.ai.decision.Check;
import server.ai.behaviour.*;

/**
 * Abstract representation of an AI-controlled unit's brain.
 * Created by Rhys on 3/8/17.
 */
public abstract class AIBrain {

    public enum EmotionalState{
        INTIMIDATED,
        VENGEFUL,
        AGGRESSIVE,
        DETERMINED,
        BORED}

    protected EmotionalState curEmotion;
    protected Intel intel;
    protected Check check;
    protected Feel feel;
    protected BehaviourSet behaviours;

    public AIBrain(Intel intel) {
        this.curEmotion = EmotionalState.BORED;
        this.intel = intel;
        this.check = new Check(intel);
        this.feel = new Feel(this);
        this.behaviours = new BehaviourSet();

    }

    protected void constructBehaviours(){
        behaviours.addBehaviour(new Dawdle(intel, this), "Dawdle");
        behaviours.addBehaviour(new FindPath(intel, this), "FindPath");
        behaviours.addBehaviour(new LocateCover(intel, this), "LocateCover");
        behaviours.addBehaviour(new Wander(intel, this), "Wander");
        behaviours.addBehaviour(new Zap(intel, this), "Zap");
    }

    protected abstract void configureBehaviours();

    protected abstract void handleEmotion();

    public abstract void doSomething();


    public void setEmotion(EmotionalState newEmotion) {
        if (newEmotion != curEmotion) {
            behaviours.resetAll();
            curEmotion = newEmotion;
            handleEmotion();
        }
    }

    /**
     * @return the current emotional state of the AI Unit this Brain belongs to.
     */
    public EmotionalState getEmotion() {
        return curEmotion;
    }

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
