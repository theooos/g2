package server.ai.decision;

import static server.ai.decision.PlayerBrain.EmotionalState.*;

/**
 * This object is used by its parent entity to analyse the results of several {@link Check}
 * object checks and thus determine the appropriate emotion for the entity's current
 * situation.
 *
 * Created by Rhys on 2/16/17.
 */
public class Feel {

    private PlayerBrain brain;
    private PlayerIntel intel;
    private Check checker;

    /**
     * Constructs a Feel object as a child of the given brain, that utilises the
     * given Intel and Check objects for deciding how the entity should react to it's
     * current situation.
     *
     * @param brain   the brain of the parent entity.
     * @param checker the object used for performing several complex checks.
     * @param intel   grants access to information about the world and the parent entity.
     */
    public Feel(PlayerBrain brain, Check checker, PlayerIntel intel){
        this.brain = brain;
        this.intel = intel;
        this.checker = checker;
    }

    /**
     * Determines the appropriate emotion for the parent entity under the current
     * circumstances and reports back to the brain.
     */
    public void doFinal() {
        // If the entity has very low health, be scared.
        if (checker.doCheck(Check.CheckMode.LOW_HEALTH)) {
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
        else if (checker.doCheck(Check.CheckMode.ENEMY_IN_PHASE)) {
            brain.setEmotion(DETERMINED);
        }
        // Otherwise, just chill.
        else {
            brain.setEmotion(BORED);
        }
    }
}
