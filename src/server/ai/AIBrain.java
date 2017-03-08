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
    }

    protected abstract void constructBehaviours();

    public abstract void doSomething();

    public abstract void emotionTransition(EmotionalState newEmotion);

    /**
     * @return the current emotional state of the AI Unit this Brain belongs to.
     */
    public EmotionalState getEmotion() {
        return curEmotion;
    }
}
