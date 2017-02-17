package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Allows the AI Unit to check if it sustained damage in the previous tick.
 * Created by rhys on 2/16/17.
 */
public class HealthCheck extends Task {

    public HealthCheck(Intel intel){
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void start() {
        System.out.println("Starting Task: HEALTH CHECK.");
    }

    @Override
    public void end() {
        System.out.println("Ending Task: HEALTH CHECK.");
    }

    @Override
    public void doAction() {
        int currentHealth = intel.ent().getHealth();
        if (currentHealth < intel.healthLastTick()){
            intel.setHurt(true);
        } else {
            intel.setHurt(false);
        }
        intel.rememberHealth(currentHealth);
        getControl().succeed();
    }

}
