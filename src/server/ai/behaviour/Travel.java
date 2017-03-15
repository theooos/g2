package server.ai.behaviour;

import server.ai.OrbTask;
import server.ai.PlayerTask;
import server.ai.decision.OrbBrain;
import server.ai.decision.OrbIntel;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.MovableEntity;
import server.game.Player;
import server.game.Vector2;

/**
 * Allows the entity to follow a path.
 * Created by rhys on 2/16/17.
 */
public class Travel extends PlayerTask {

    private int stress;
    public static final int DEFAULT_STRESS = 1;

    public Travel(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        stress = DEFAULT_STRESS;
    }

    public void setParameters(int stress){
        this.stress = stress;
    }

    @Override
    public void reset(){
        super.reset();
        stress = DEFAULT_STRESS;
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
        me.setDir(Vector2.deviate(target, stress));

        // Checks for collisions,
        Vector2 oldPos = me.getPos();
        me.setPos(oldPos.add(me.getDir().mult(me.getSpeed())));
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
        System.err.println("Float is not a single-tick task.");
        System.exit(1);
    }
}
