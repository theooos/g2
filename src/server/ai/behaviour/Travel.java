package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Created by rhys on 2/16/17.
 */
public class Travel extends Task {

    public Travel(Intel env, MovableEntity ent){
        super(env, ent);
    }

    @Override
    public boolean checkConditions() {
        return (ent.isAlive() &&
                env.getTargetLocation() != null &&
                env.checkpoint() != null);
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
        // Sets the entity to face in direction of the target location.
        ent.setDir((ent.getPos().vectorTowards(env.checkpoint())).normalise());

        // Moves the entity.
        ent.setPos(ent.getPos().add(ent.getDir().mult(ent.getSpeed())));

        // Check if the checkpoint has been reached.
        boolean reached = env.checkpoint().getDistanceTo(ent.getPos())
                <= ent.getRadius();

        // Update the entity's state for the next tick.
        if (reached && env.isFinalDestination()){
            getControl().succeed();
        } else if (reached) {
            env.nextCheckpoint();
        }


    }


}
