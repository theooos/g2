package server.ai.behaviour;

import server.ai.behaviour.Behaviour;

/**
 * Stores information about the state of the task under this controller's control.
 * Created by rhys on 2/16/17.
 */
public class TaskController {

    protected boolean finished;
    protected boolean success;
    protected boolean started;
    protected boolean requiresRepeat;
    protected Behaviour behaviour;

    public TaskController(Behaviour behaviour, boolean requiresRepeat){
        this.finished = false;
        this.success = true;
        this.started = false;
        this.requiresRepeat = requiresRepeat;
        this.behaviour = behaviour;
    }

    public void safeStart() {
        this.started = true;
        behaviour.start();
    }

    public void safeEnd() {
        this.finished = false;
        this.started = false;
        behaviour.end();
    }

    protected void succeed() {
        this.success = true;
        this.finished = !requiresRepeat;
    }

    protected void fail() {
        this.success = false;
        this.finished = !requiresRepeat;
    }

    public boolean succeeded() {
        return success;
    }

    public boolean failed() {
        return !success;
    }

    public boolean finished() {
        return finished;
    }

    public boolean started() {
        return started;
    }

    public void reset() {
        this.finished = false;
    }

    public void setBehaviour(Behaviour behaviour){
        this.behaviour = behaviour;
    }
}
