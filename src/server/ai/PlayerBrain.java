package server.ai;

import server.ai.behaviour.Sequence;

/**
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    private Sequence flee;
    private Sequence rage;
    private Sequence attack;
    private Sequence chase;
    private Sequence hunt;

    public PlayerBrain(Intel intel) {
        super(intel);
    }

    @Override
    protected void configureBehaviours() {

    }

    @Override
    public void doSomething() {

    }

    @Override
    public void emotionTransition(EmotionalState newEmotion) {

    }
}
