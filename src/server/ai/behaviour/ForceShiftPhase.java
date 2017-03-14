package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Forces the AI Player to shift phase by checking for success in the next tick
 * and, upon failure, moving slightly and trying again.
 * Created by Rhys on 3/11/17.
 */
public class ForceShiftPhase extends PlayerTask {

    public ForceShiftPhase(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().getPhase() == 0) || (intel.ent().getPhase() == 1);
    }

    @Override
    public void doAction() {

        // If re-attempt is currently ongoing:
        if (brain.getBehaviour("QuickMove").isRunning()){
            brain.getBehaviour("QuickMove").doAction();
        }
        // If a phase-shift hasn't been attempted yet, try it.
        else if (!intel.phaseShiftAttempted()) {
            brain.getBehaviour("ShiftPhase").run();
        }
        // If it has, check for failure.
        else {
            // If phase-shift failed:
            if (!intel.phaseShiftSuccessful()){
                // If re-attempt has also failed, give up.
                if (brain.getBehaviour("QuickMove").hasFinished()){
                    end();
                }
                // if trying for the first time:
                else {
                    brain.getBehaviour("QuickMove").start();
                }
            }
            // If succeeded:
            else {
                end();
            }
        }
    }
}
