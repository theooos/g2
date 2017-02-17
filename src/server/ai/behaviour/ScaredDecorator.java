package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;

/**
 * Adds "Scared" checks to the behaviour it is applied to.
 * Created by rhys on 2/16/17.
 */
public class ScaredDecorator extends Decorator {

    private final int MINIMUM_HEALTH = 50;

    public ScaredDecorator(Intel intel, Behaviour behaviour){
        super(intel, behaviour);
    }

    @Override
    public void doAction(){
        behaviour.doAction();
    }

    @Override
    public boolean checkConditions() {
        System.out.println("In pain: " + intel.isInPain());
        System.out.println("Superconditions: " + super.checkConditions());
        System.out.println("Health: " + intel.ent().getHealth());
        return super.checkConditions() &&
                (intel.isInPain() || intel.ent().getHealth() < 50);
    }

}