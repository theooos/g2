package server.ai.behaviour;

import server.ai.OrbTask;
import server.ai.PlayerTask;
import server.ai.decision.*;
import server.game.MovableEntity;
import server.game.Player;
import server.game.Vector2;

/**
 * Allows the entity to follow a path.
 * Created by Rhys on 2/16/17.
 */
public class Travel extends PlayerTask {

    public Travel(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().isAlive() &&
                intel.getTargetLocation() != null &&
                intel.checkpoint() != null);
    }

    @Override
    public void doAction() {

        Player me = intel.ent();

        // Sets the entity to face in direction of the target location.
        Vector2 target = me.getPos().vectorTowards(intel.checkpoint());
        int inaccuracy = (int) Math.ceil(brain.getStressLevel()* AIConstants.MAX_TRAVEL_INACCURACY);
        Vector2 travelDir = Vector2.deviate(target, inaccuracy);

        // Checks for collisions,
        Vector2 oldPos = me.getPos();
        me.setDir(intel.getPointerVector());
        me.setPos(oldPos.add(travelDir.mult(me.getSpeed())));
        if (!intel.validPosition()) {
            me.setPos(oldPos);
            end();
        } else {
            // Check if the checkpoint has been reached.
            float distance = me.getPos().getDistanceTo(intel.checkpoint());
            distance = distance - me.getRadius();
            boolean reached = distance <= 0;

            // Update the entity's state for the next tick.
            if (reached && intel.isFinalDestination()){
                end();
            } else if (reached) {
                intel.nextCheckpoint();
            }
        }

    }

    @Override
    public void run(){
        System.err.println("Travel is not a single-tick task.");
        System.exit(1);
    }
}
