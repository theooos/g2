package server.ai;

import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Represents a leaf task of the Behaviour Tree that's restricted
 * for use by Players only.
 *
 * Created by Rhys on 2/16/17.
 */
public abstract class PlayerTask extends Task {

    protected PlayerIntel intel;
    protected PlayerBrain brain;

    /**
     * Constructs an player-only behaviour that will use the given intelligence object.
     *
     * @param intel the intel object that will be used for making decisions and
     *              causing actions.
     * @param brain the brain of the owning entity of this behaviour.
     */
    public PlayerTask(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
        this.intel = intel;
        this.brain = brain;
    }
}
