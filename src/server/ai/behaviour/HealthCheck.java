package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Allows the AI Unit to check if it sustained damage in the previous tick.
 * Created by rhys on 2/16/17.
 */
public class HealthCheck extends Task {

    public HealthCheck(Intel env, MovableEntity ent){
        super(env, ent);
    }

    @Override
    public boolean checkConditions() {
        return ent.isAlive();
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
        int currentHealth = ent.getHealth();
        if (currentHealth < env.healthLastTick()){
            env.setHurt(true);
        } else {
            env.setHurt(false);
        }
        env.rememberHealth(currentHealth);
        getControl().suceed();
    }

}
