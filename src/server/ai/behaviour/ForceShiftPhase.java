package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * This behaviour allows AI-controlled players to insist upon a phase-shift.
 * The player will attempt to phase-shift and make use of the {@link QuickMove}
 * behaviour to facilitate another phase-shift attempt. If the second attempt
 * fails, the behaviour will give up.
 *
 * Created by Rhys on 3/11/17.
 */
public class ForceShiftPhase extends PlayerTask {

    /**
     * Constructs a ForceShiftPhase behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     */
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
