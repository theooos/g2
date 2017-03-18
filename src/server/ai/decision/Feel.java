package server.ai.decision;

import server.ai.AIBrain;

import static server.ai.decision.PlayerBrain.EmotionalState.*;

/**
 * Sets the emotional state of the player, depending on some external factors.
 * Created by rhys on 2/16/17.
 */
public class Feel {

    private PlayerBrain brain;
    private PlayerIntel intel;
    private Check checker;

    public Feel(PlayerBrain brain, Check checker, PlayerIntel intel){
        this.brain = brain;
        this.intel = intel;
        this.checker = checker;
    }

    public void doFinal() {
        // If the entity has very low health, be scared.
        if (checker.doCheck(Check.CheckMode.LOW_HEALTH) && !intel.haveEscaped()) {
            brain.setEmotion(INTIMIDATED);
        }
        // If there is an Orb nearby (in either phase).
        else if (checker.doCheck(Check.CheckMode.ORB_NEARBY)) {
            brain.setEmotion(IRRITATED);
        }
        // If there is an enemy nearby, be angry.
        else if (checker.doCheck(Check.CheckMode.PROXIMITY)) {
            brain.setEmotion(AGGRESSIVE);
        }
        // Otherwise, just chill.
        else {
            brain.setEmotion(BORED);
        }
    }


}
