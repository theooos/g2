package server.ai;

import server.ai.behaviour.*;

/**
 * Controls the actions of an Orb.
 * Created by rhys on 2/20/17.
 */
public class OrbBrain {

    public enum EmotionalState{SCARED, ANGRY, RELAXED}
    private EmotionalState curEmotion;

    private Intel intel;
    private Check check;
    private Feel feel;
    private Sequence flee;
    private Sequence drift;
    private FindPath pathfinder;
    private Travel traveller;
    private Zap zapper;

    public OrbBrain(Intel intel) {
        this.intel = intel;
        this.curEmotion = EmotionalState.RELAXED;
        constructBehaviours();
    }

    private void constructBehaviours(){

        this.feel = new Feel(intel, this);
        this.check = new Check(intel);

        this.drift = new Sequence(intel, this);
        this.drift.add(new Wander(intel, this));
        this.drift.add(new FindPath(intel, this));
        this.drift.add(new Travel(intel, this));

        this.flee = new Sequence(intel, this);
        this.flee.add(new LocateCover(intel, this));
        this.flee.add(new FindPath(intel, this));
        this.flee.add(new Travel(intel, this));

        this.pathfinder = new FindPath(intel, this);
        this.traveller = new Travel(intel, this);
        this.zapper = new Zap(intel, this);
    }

    public void doSomething(){

        // Perform checks.
        boolean inPain = check.doCheck(Check.CheckMode.HEALTH);
        boolean playerNear = check.doCheck(Check.CheckMode.PROXIMITY);

        // Find emotion.
        feel.setParameters(inPain, playerNear);
        feel.run();

        // Decide what to do.
        if (curEmotion == EmotionalState.SCARED){
            flee.start();
        } else if (curEmotion == EmotionalState.ANGRY) {
            // If the target player has moved since the last tick:
            if (check.doCheck(Check.CheckMode.TARGET_MOVED)) {
                intel.setTargetLocation(intel.getTargetPlayer().getPos());
                pathfinder.run();
                traveller.start();
            } else {
                // If the target player is outside of attacking range:
                if (!check.doCheck(Check.CheckMode.RANGE)) {
                    traveller.doAction();
                }
                else {
                    zapper.run();
                }
            }

        } else if (curEmotion == EmotionalState.RELAXED) {
            drift.start();
        }

    }

    public EmotionalState getEmotion(){
        return curEmotion;
    }

    /**
     * If the orb has experienced a change in emotion, reset some of its knowledge.
     * @param newEmotion - the Orb's new emotion.
     */
    public void emotionTransition(EmotionalState newEmotion){
        if (newEmotion != curEmotion) {
            flee.reset();
            drift.reset();
            pathfinder.reset();
            zapper.reset();
            traveller.reset();

            curEmotion = newEmotion;
        }
    }
}
