package server.ai.behaviour;

import server.ai.Intel;
import server.game.Orb;
import server.game.Player;

/**
 * Allows the entity to perform a very powerful, touching-range attack.
 * Restricted for use by Orbs only.
 * Created by rhys on 2/16/17.
 */
public class Zap extends Attack {

    public Zap(Intel intel) {
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        System.out.println("Orb thinks player is at " + intel.getTargetPlayer().getPos());
        System.out.println("Orb thinks Orb is at " + intel.ent().getPos());
        System.out.println("Orb thinks distance from player is " + intel.ent().getPos().getDistanceTo(intel.getTargetPlayer().getPos()));
        return (intel.ent().getPos().getDistanceTo(intel.getTargetPlayer().getPos())
                < intel.ent().getRadius()) &&
                (intel.ent() instanceof Orb);
    }

    @Override
    public void doAction() {
        Player target = intel.getTargetPlayer();
        target.damage(45);
        getControl().succeed();

    }
}
