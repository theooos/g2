package server.ai.behaviour;

import server.ai.Intel;
import server.game.Orb;

/**
 * Adds "Angry" checks to the behaviour it is applied to.
 * Created by rhys on 2/16/17.
 */
public class AngryDecorator extends Decorator {

    public AngryDecorator(Intel intel, Behaviour behaviour) {
        super(intel, behaviour);
    }

    @Override
    public void doAction() {
        behaviour.doAction();
    }

    @Override
    public boolean checkConditions() {
        return super.checkConditions() &&
                intel.isPlayerNearby();
    }
}
