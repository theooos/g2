package server.ai;

import server.ai.decision.OrbBrain;
import server.ai.decision.OrbIntel;

/**
 * Represents a leaf task of the Behaviour Tree.
 * Created by rhys on 2/16/17.
 */
public abstract class OrbTask extends Task {

    protected OrbIntel intel;
    protected OrbBrain brain;

    public OrbTask(OrbIntel intel, OrbBrain brain) {
        super(intel, brain);
        this.intel = intel;
        this.brain = brain;
    }
}
