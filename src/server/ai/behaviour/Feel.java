package server.ai.behaviour;

import server.ai.Intel;
import server.ai.OrbBrain;

/**
 * Sets the emotional state of the entity, depending on some external factors.
 * Created by rhys on 2/16/17.
 */
public class Feel extends Task {

    private boolean inPain;
    private boolean playerNearby;

    public Feel(Intel intel, OrbBrain brain){
        super(intel, brain);
        inPain = false;
        playerNearby = false;
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    /**
     * Feeds this task the information it needs to decide how the entity is feeling.
     * @param inPain - Whether or not the entity has lost health since the last tick.
     * @param playerNearby - Whether or not there is an enemy player in the entity's field of vision.
     */
    public void setParameters(boolean inPain, boolean playerNearby){
        this.inPain = inPain;
        this.playerNearby = playerNearby;
    }

    @Override
    public void doAction() {

        // If the entity has lost health since the last tick, be scared.
        if (inPain){
            brain.emotionTransition(OrbBrain.EmotionalState.SCARED);
        }
        // If there is an enemy nearby, be angry.
        else if (playerNearby) {
            brain.emotionTransition(OrbBrain.EmotionalState.ANGRY);
        }
        // Otherwise, just chill.
        else {
            brain.emotionTransition(OrbBrain.EmotionalState.RELAXED);
        }
    }


}
