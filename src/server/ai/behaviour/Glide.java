package server.ai.behaviour;

import server.ai.OrbTask;
import server.ai.decision.OrbBrain;
import server.ai.decision.OrbIntel;
import server.game.MovableEntity;
import server.game.Vector2;

/**
 * This behaviour allows an Orb to move towards a pre-determined checkpoint, once per tick.
 * <p>
 * Created by rhys on 2/16/17.
 */
public class Glide extends OrbTask {

    /**
     * Constructs a Glide behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     */
    public Glide(OrbIntel intel, OrbBrain brain){
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
        System.err.println("Glide is not a single-tick task.");
        System.exit(1);
    }
}
