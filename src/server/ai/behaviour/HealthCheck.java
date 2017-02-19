package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Allows the AI Unit to check if it sustained damage in the previous tick.
 * Created by rhys on 2/16/17.
 */
public class HealthCheck extends Task {

    public HealthCheck(Intel intel){
        super(intel, "Health Check");
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
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
