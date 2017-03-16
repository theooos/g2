package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Allows the AI Player to shift phases.
 * Created by Rhys on 3/11/17.
 */
public class ShiftPhase extends PlayerTask {

    private boolean overrideAuth;

    public ShiftPhase(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        overrideAuth = false;
    }

    public void forceAction(){
        overrideAuth = true;
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().getPhase() == 0) || (intel.ent().getPhase() == 1);
    }

    @Override
    public void doAction() {
        if (brain.phaseShiftAuth() || overrideAuth) {
            if (intel.ent().getPhase() == 0) {
                intel.ent().setPhase(1);
                if (!intel.validPosition()) {
                    intel.ent().setPhase(0);
                    intel.failedPhaseShift();
                }
            } else if (intel.ent().getPhase() == 1) {
                intel.ent().setPhase(0);
                if (!intel.validPosition()) {
                    intel.ent().setPhase(0);
                    intel.failedPhaseShift();
                }
            }
            overrideAuth = false;
            brain.shiftedPhase();
        }
        end();
    }
}
