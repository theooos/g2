package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Allows the AI Player to shift phases.
 * Created by Rhys on 3/11/17.
 */
public class ShiftPhase extends PlayerTask {

    public ShiftPhase(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().getPhase() == 0) || (intel.ent().getPhase() == 1);
    }

    @Override
    public void doAction() {
        if (intel.ent().getPhase() == 0){
            intel.ent().setPhase(1);
            intel.attemptedPhaseShift(0);
        }
        else if (intel.ent().getPhase() == 1){
            intel.ent().setPhase(0);
            intel.attemptedPhaseShift(1);
        }
        end();
    }
}
