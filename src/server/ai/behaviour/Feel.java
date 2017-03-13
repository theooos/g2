package server.ai.behaviour;

import server.ai.AIBrain;

/**
 * Sets the emotional state of the entity, depending on some external factors.
 * Created by rhys on 2/16/17.
 */
public class Feel {

    private boolean lowHealth;
    private boolean playerNearby;
    private AIBrain brain;

    public Feel(AIBrain brain){
        lowHealth = false;
        playerNearby = false;
        this.brain = brain;
    }

    /**
     * Feeds this task the information it needs to decide how the entity is feeling.
     * @param lowHealth - Whether or not the entity has lost health since the last tick.
     * @param playerNearby - Whether or not there is an enemy player in the entity's field of vision.
     */
    public void setParameters(boolean lowHealth, boolean playerNearby){
        this.lowHealth = lowHealth;
        this.playerNearby = playerNearby;
    }

    public void doFinal() {

        // If the entity has lost health since the last tick, be scared.
        if (lowHealth){
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
