package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * This behaviour allows an AI-controlled player to move a short-distance
 * in the opposite direction of a threat, in order to facilitate a reattempt
 * to shift phase by the {@link ForceShiftPhase} behaviour.
 * <p>
 * If the player is unable to move backwards due to an obstruction, this
 * behaviour will try attempt five times to find an alternative short-distance
 * destination irrespective of the direction.
 *
 * Created by Rhys on 2/16/17.
 */
public class QuickMove extends PlayerTask {

    /**
     * Constructs a QuickMove behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     */
    public QuickMove(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }


    @Override
    public void start() {

        super.start();

        boolean success = false;

        // First, attempt to find a suitable place in the opposite direction
        // from the assailant.
        if (brain.performCheck(Check.CheckMode.CLOSEST_ENEMY)){
            Vector2 dest = intel.ent().getPos().vectorTowards(intel.getRelevantEntity().getPos());
            dest = intel.ent().getPos().add(new Vector2(0,0).sub(dest.clampedTo(25)));
            if (intel.inSight(dest)){
                intel.setTargetLocation(dest);
                success = true;
            }
        }
        if (!success) {
            // Failing that, try 5 random destinations.
            for (int i = 0; i < 5; i ++) {
                Vector2 dest = Vector2.randomVector(25);
                if (intel.inSight(dest)){
                    intel.setTargetLocation(dest);
                    success = true;
                    break;
                }
            }
        }

        if (!success) {
            // If still no success, give up and attempt to find cover in
            // the current phase.
            end();
        } else {
            ((FindPath)(brain.getBehaviour("FindPath"))).setSimplePath(true);
            brain.getBehaviour("FindPath").run();
            brain.getBehaviour("Travel").start();
        }
    }

    @Override
    public void doAction(){
        if (brain.getBehaviour("Travel").hasFinished()){
            brain.getBehaviour("Travel").reset();
            brain.getBehaviour("FindPath").reset();
            end();
        }
        else {
            brain.getBehaviour("Travel").doAction();
        }
    }


}
