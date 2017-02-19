package server.ai.behaviour;

import server.ai.Intel;

/**
 * Provides additional starting conditions for the "Attack" sequence.
 * Created by rhys on 2/18/17.
 */
public class AttackDecorator extends Decorator {

    public AttackDecorator(Intel intel, Behaviour behaviour){
        super(intel, behaviour);
    }


    @Override
    public void doAction() {
        behaviour.doAction();
    }

    @Override
    public boolean checkConditions(){
        return (intel.isTargetAcquired() &&
                intel.checkpoint()!= null);
    }
}
