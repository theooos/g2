package server.ai.behaviour.decorator;

import server.ai.Intel;
import server.ai.behaviour.Behaviour;
import server.ai.behaviour.TaskController;

/**
 * Created by rhys on 2/16/17.
 */
public abstract class Decorator extends Behaviour {

    public Behaviour behaviour;

    public Decorator(Intel intel, Behaviour behaviour){
        super(intel);
        this.behaviour = behaviour;
        this.behaviour.getControl().setBehaviour(this);
    }

    @Override
    public boolean checkConditions() {
        return behaviour.checkConditions();
    }

    @Override
    public void start() {
        behaviour.start();
    }

    @Override
    public void end() {
        behaviour.end();
    }

    @Override
    public TaskController getControl() {
        return behaviour.getControl();
    }
}
