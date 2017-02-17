package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Allows the entity to follow a path.
 * Created by rhys on 2/16/17.
 */
public class Travel extends Task {

    public Travel(Intel intel){
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().isAlive() &&
                intel.getTargetLocation() != null &&
                intel.checkpoint() != null);
    }

    @Override
    public void start() {
        System.out.println("Starting Task: TRAVEL.");

    }

    @Override
    public void end() {
        System.out.println("Ending Task: TRAVEL.");

    }

    @Override
    public void doAction() {

        MovableEntity ent = intel.ent();
        // Sets the entity to face in direction of the target location.
        ent.setDir((ent.getPos().vectorTowards(intel.checkpoint())).normalise());

        // Moves the entity.
        ent.setPos(ent.getPos().add(ent.getDir().mult(ent.getSpeed())));

        // Check if the checkpoint has been reached.
        boolean reached = intel.checkpoint().getDistanceTo(ent.getPos())
                <= ent.getRadius();

        // Update the entity's state for the next tick.
        if (reached && intel.isFinalDestination()){
            getControl().succeed();
        } else if (reached) {
            intel.nextCheckpoint();
        }


    }


}
