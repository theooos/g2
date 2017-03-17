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

        System.out.println("Forcing phase shift.");

        // If re-attempt is currently ongoing:
        if (brain.getBehaviour("QuickMove").isRunning()){
            brain.getBehaviour("QuickMove").doAction();
        }
        else {
            ((ShiftPhase)brain.getBehaviour("ShiftPhase")).forceAction();
            brain.getBehaviour("ShiftPhase").run();
            if (intel.phaseShiftFailed()) {
                if (brain.getBehaviour("QuickMove").hasFinished()) {
                    end();
                }
                else {
                    brain.getBehaviour("QuickMove").start();
                }
            }
            else {
                end();
            }
        }
    }
}
