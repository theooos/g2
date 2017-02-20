package server.ai.behaviour;

import server.ai.Intel;

/**
 * Represents a leaf task of the Behaviour Tree.
 * Created by rhys on 2/16/17.
 */
public abstract class Task {

    public enum runState{DORMANT, RUNNING, FINISHED}
    protected runState curRunState;
    protected Intel intel;

    public Task(Intel intel) {
        this.intel = intel;
        this.curRunState = runState.DORMANT;
    }

    /**
     * Check whether or not the behaviour's initial conditions are met.
     * @return true if the behaviour is permissible.
     */
    public abstract boolean checkConditions();

    /**
     * Performs the startup logic of the behaviour.
     */
    public void start() {
        this.curRunState = runState.RUNNING;
    }

    /**
     * Performs the updating logic the behaviour must perform each cycle.
     */
    public abstract void doAction();

    /**
     * Performs the ending logic of the behaviour.
     */
    public void end(){
        this.curRunState = runState.FINISHED;
    }

    /**
     * Resets the state of this task.
     */
    public void reset() {
        this.curRunState = runState.DORMANT;
    }

    /**
     * @return true if this task has not yet started.
     */
    public boolean isDormant(){
        return (curRunState == runState.DORMANT);
    }

    /**
     * @return true if this task is currently running.
     */
    public boolean isRunning(){
        return (curRunState == runState.RUNNING);
    }

    /**
     * @return true if this task has finished.
     */
    public boolean hasFinished(){
        return (curRunState == runState.FINISHED);
    }

}
