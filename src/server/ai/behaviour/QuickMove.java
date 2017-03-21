package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.PlayerTask;
import server.ai.Task;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

import java.util.Random;

/**
 * Picks a very close destination for the orb to move towards, in attempt
 * to clear a wall in the other phase, and moves towards it.
 * Created by rhys on 2/16/17.
 */
public class QuickMove extends PlayerTask {

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
