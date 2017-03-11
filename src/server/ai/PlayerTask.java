package server.ai;

import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Represents a leaf task of the Behaviour Tree.
 * Created by rhys on 2/16/17.
 */
public abstract class PlayerTask extends Task {

    protected PlayerIntel intel;
    protected PlayerBrain brain;

    public PlayerTask(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
        this.intel = intel;
        this.brain = brain;
    }
}
