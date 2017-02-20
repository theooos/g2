package server.ai.behaviour.decorator;

import server.ai.Intel;
import server.ai.behaviour.Behaviour;
import server.ai.behaviour.decorator.Decorator;

/**
 * Makes the entity repeat the given behaviour.
 * Created by rhys on 2/16/17.
 */
public class ResetDecorator extends Decorator {

    public ResetDecorator(Intel intel, Behaviour behaviour){
        super(intel, behaviour);
    }

    @Override
    public void doAction() {
        this.behaviour.doAction();
        if (this.behaviour.getControl().finished()){
            this.behaviour.getControl().reset();
        }
    }
}
