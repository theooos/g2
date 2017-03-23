package server.ai;

import server.ai.decision.OrbBrain;
import server.ai.decision.OrbIntel;

/**
 * Represents a leaf task of the Behaviour Tree that's restricted
 * for use by Orbs only.
 *
 * Created by Rhys on 2/16/17.
 */
public abstract class OrbTask extends Task {

    protected OrbIntel intel;
    protected OrbBrain brain;

    /**
     * Constructs an orb-only behaviour that will use the given intelligence object.
     *
     * @param intel the intel object that will be used for making decisions and
     *              causing actions.
     * @param brain the brain of the owning entity of this behaviour.
     */
    public OrbTask(OrbIntel intel, OrbBrain brain) {
        super(intel, brain);
        this.intel = intel;
        this.brain = brain;
    }
}
