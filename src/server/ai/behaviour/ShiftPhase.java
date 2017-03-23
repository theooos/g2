package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * This behaviour allows an AI-controlled player to shift-phase, but will
 * fail if the players resulting position in the other phase is invalid.
 * Failure can be detected by calling the {@link PlayerIntel#phaseShiftFailed()}
 * method.
 * <p>
 * If shift-phasing is absolutely required in a particular situation, the
 * {@link ForceShiftPhase} behaviour should be used instead.
 * <p>
 * Created by Rhys on 3/11/17.
 */
public class ShiftPhase extends PlayerTask {

    private boolean overrideAuth;

    /**
     * Constructs a ShiftPhase behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
    public ShiftPhase(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        overrideAuth = false;
    }

    /**
     * Allows another behaviour to override the brain's phase-shift delay timer
     * when called.
     */
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

            intel.ent().setPhase(1 - intel.ent().getPhase());
            if (!intel.validPosition()) {
                intel.ent().setPhase(1 - intel.ent().getPhase());
                intel.failedPhaseShift();
            } else {
                overrideAuth = false;
                brain.shiftedPhase();
            }
            end();
        }
    }
}
