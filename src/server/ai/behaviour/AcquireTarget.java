package server.ai.behaviour;

import server.ai.Intel;

/**
 * Created by rhys on 2/16/17.
 */
public class AcquireTarget extends Task {

    public AcquireTarget(Intel intel) {
        super(intel, "Acquire Target");
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void doAction() {
        // SKELETON FUNCTION.
        // PERQUISITE: CollisionDetection, PlayerSearch.
        System.out.println("Acquire target starting.");
        intel.setTargetPlayer(intel.getPlayer(0));
        intel.setTargetAcquired(true);
        getControl().succeed();
    }


}
