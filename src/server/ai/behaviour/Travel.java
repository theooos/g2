package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.*;
import server.game.Player;
import server.game.Vector2;

/**
 * This behaviour allows AI-controlled players to move towards a pre-determined
 * checkpoint, once per tick.
 * <p>
 * Travelling using this behaviour allows the exhibition of random error, which
 * is calculated by the {@link Vector2#deviate} method, based on
 * the player's stress level at the time.
 *
 * Created by Rhys on 2/16/17.
 */
public class Travel extends PlayerTask {

    /**
     * Constructs a Travel behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     */
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

        // Checks for collisions.
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
