package server.ai.decision;

import server.ai.AIBrain;

/**
 * Sets the emotional state of the player, depending on some external factors.
 * Created by rhys on 2/16/17.
 */
public class Feel {

    private AIBrain brain;
    private Check checker;

    public Feel(AIBrain brain, Check checker){
        this.brain = brain;
        this.checker = checker;
    }

    public void doFinal() {
        // If the entity has very low health, be scared.
        if (checker.doCheck(Check.CheckMode.LOW_HEALTH)) {
            brain.setEmotion(AIBrain.EmotionalState.INTIMIDATED);
        }
        // If there is an Orb nearby (in either phase).
        else if (checker.doCheck(Check.CheckMode.ORB_NEARBY)) {
            brain.setEmotion(AIBrain.EmotionalState.IRRITATED);
        }
        // If there is an enemy nearby, be angry.
        else if (checker.doCheck(Check.CheckMode.PROXIMITY)) {
            brain.setEmotion(AIBrain.EmotionalState.AGGRESSIVE);
        }
        // Otherwise, just chill.
        else {
            brain.setEmotion(AIBrain.EmotionalState.BORED);
        }
    }


}
