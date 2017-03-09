package server.ai;

import server.ai.behaviour.*;

/**
 * Created by rhys on 3/8/17.
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
        this.intel = intel;
        this.curEmotion = EmotionalState.BORED;
        this.feel = new Feel(this);
        this.check = new Check(intel);
        constructBehaviours();
        configureBehaviours();
    }

    private void constructBehaviours(){
        behaviours.addBehaviour(new Dawdle(intel, this), "Dawdle");
        behaviours.addBehaviour(new FindPath(intel, this), "FindPath");
        behaviours.addBehaviour(new LocateCover(intel, this), "LocateCover");
        behaviours.addBehaviour(new Travel(intel, this), "Travel");
        behaviours.addBehaviour(new Wander(intel, this), "Wander");
        behaviours.addBehaviour(new Zap(intel, this), "Zap");
    }

    protected abstract void configureBehaviours();

    public abstract void doSomething();

    public abstract void emotionTransition(EmotionalState newEmotion);

    /**
     * @return the current emotional state of the AI Unit this Brain belongs to.
     */
    public EmotionalState getEmotion() {
        return curEmotion;
    }
}
