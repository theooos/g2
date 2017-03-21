package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * Allows the player to hunt down a power up.
 * Created by rhys on 3/20/17.
 */
public class Fetch extends PlayerTask {

    boolean pathFound;

    public Fetch(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
        pathFound = false;
    }

    @Override
    public boolean checkConditions() {
        return true;
    }

    @Override
    public void start(){

        super.start();

        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(false);

        if (!intel.isPhaseShiftReq()){
            intel.setTargetLocation(intel.getRelevantEntity().getPos());
            brain.getBehaviour("FindPath").run();
            brain.getBehaviour("Travel").start();
            pathFound = true;
        }
        else {
            pathFound = false;
        }
    }


    @Override
    public void doAction() {

        ForceShiftPhase forceSP = (ForceShiftPhase) brain.getBehaviour("ForceShiftPhase");

        // If the power-up has disappeared, give up.
        if (!intel.getRelevantEntity().isAlive()){
            end();
            return;
        }

        if (!pathFound) {
            if (forceSP.hasFinished()){
                intel.setTargetLocation(intel.getRelevantEntity().getPos());
                brain.getBehaviour("FindPath").run();
                brain.getBehaviour("Travel").start();
                pathFound = true;
            }
            else {
                if (forceSP.isDormant()) forceSP.start();
                forceSP.doAction();
            }
        }
        else {
            brain.getBehaviour("Travel").doAction();
        }
    }
}
