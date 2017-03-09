package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.OrbBrain;

/**
 * Sets the emotional state of the entity, depending on some external factors.
 * Created by rhys on 2/16/17.
 */
public class Feel {

    private boolean inPain;
    private boolean playerNearby;
    private AIBrain brain;

    public Feel(AIBrain brain){
        inPain = false;
        playerNearby = false;
        this.brain = brain;
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

    public void doFinal() {

        // If the entity has lost health since the last tick, be scared.
        if (inPain){
            brain.setEmotion(AIBrain.EmotionalState.INTIMIDATED);
        }
        // If there is an enemy nearby, be angry.
        else if (playerNearby) {
            brain.setEmotion(AIBrain.EmotionalState.AGGRESSIVE);
        }
        // Otherwise, just chill.
        else {
            brain.setEmotion(AIBrain.EmotionalState.BORED);
        }
    }


}
