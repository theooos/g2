package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

/**
 * This behaviour allows an AI-controlled player to hunt and collect a
 * pre-targeted power-up, shifting phase if necessary.
 *
 * Created by Rhys on 3/20/17.
 */
public class Fetch extends PlayerTask {

    private boolean pathFound;

    /**
     * Constructs a Fetch behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
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

    /**
     * Prevents this behaviour from being run as a single-tick-task.
     */
    @Override
    public void run(){
        System.err.println("Fetch is not a single-tick task.");
        System.exit(1);
    }
}
