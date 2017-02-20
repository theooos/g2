package server.ai.behaviour.decorator;

import server.ai.Intel;
import server.ai.behaviour.Behaviour;
import server.ai.behaviour.Travel;

/**
 * Provides additional starting conditions for the "Attack" sequence.
 * Created by rhys on 2/18/17.
 */
public class HuntDecorator extends Decorator {

    public HuntDecorator(Intel intel, Behaviour travelInstance){
        super(intel, travelInstance);
    }


    @Override
    public void doAction() {
        behaviour.doAction();
    }

    @Override
    public boolean checkConditions(){
        System.out.println("Player moved:" + playerMoved());
        return (behaviour.checkConditions() && !playerMoved());

    }

    private boolean playerMoved(){
        return (intel.getTargetPlayer().getPos().equals(intel.getTargetLocation()));
    }
}
