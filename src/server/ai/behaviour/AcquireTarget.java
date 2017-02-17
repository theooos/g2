package server.ai.behaviour;

import server.ai.Intel;

/**
 * Created by rhys on 2/16/17.
 */
public class AcquireTarget extends Task {

    public AcquireTarget(Intel intel) {
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void start() {
        System.out.println("Starting task: ACQUIRE TARGET");
    }

    @Override
    public void end() {
        System.out.println("Ending task: ACQUIRE TARGET");
    }

    @Override
    public void doAction() {
        // SKELETON FUNCTION.
        // PERQUISITE: CollisionDetection, PlayerSearch.
        intel.setTargetPlayer(intel.getPlayer(0));
    }


}
