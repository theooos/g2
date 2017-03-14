package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.OrbTask;
import server.ai.Task;
import server.ai.decision.OrbBrain;
import server.ai.decision.OrbIntel;
import server.ai.decision.PlayerBrain;
import server.game.MovableEntity;
import server.game.Vector2;

import java.util.Random;

/**
 * Allows the entity to follow a path.
 * Created by rhys on 2/16/17.
 */
public class Float extends OrbTask {

    public Float(OrbIntel intel, OrbBrain brain){
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

        MovableEntity ent = intel.ent();
        // Sets the entity to face in direction of the target location.
        Vector2 target = ent.getPos().vectorTowards(intel.checkpoint())/*.normalise()*/;
        ent.setDir(Vector2.deviate(target, 30));

        // Checks for collisions,
        Vector2 oldPos = ent.getPos();
        ent.setPos(oldPos.add(ent.getDir().mult(ent.getSpeed())));
        if (!intel.validPosition()) {
            ent.setPos(oldPos);
            end();
        } else {
            // Check if the checkpoint has been reached.
            float distance = ent.getPos().getDistanceTo(intel.checkpoint());
            distance = distance - ent.getRadius();
            boolean reached = distance <= 35;

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
        System.err.println("Float is not a single-tick task.");
        System.exit(1);
    }
}
