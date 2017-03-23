package server.ai;

/**
 * Represents a leaf task of the Behaviour Tree.
 *
 * Created by Rhys on 2/16/17.
 */
public abstract class Task {

    public enum runState{DORMANT, RUNNING, FINISHED}
    protected runState curRunState;
    protected Intel intel;
    protected AIBrain brain;

    /**
     * Constructs an generic behaviour that will use the given intelligence object.
     *
     * @param intel the intel object that will be used for making decisions and
     *              causing actions.
     * @param brain the brain of the owning entity of this behaviour.
     */
    public Task(Intel intel, AIBrain brain) {
        this.intel = intel;
        this.brain = brain;
        this.curRunState = runState.DORMANT;
    }

    /**
     * Check whether or not the behaviour's initial conditions are met.
     * @return <CODE>true</CODE> if the behaviour is permissible.
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
     * @return <CODE>true</CODE> if this task has not yet started.
     */
    public boolean isDormant(){
        return (curRunState == runState.DORMANT);
    }

    /**
     * @return <CODE>true</CODE> if this task is currently running.
     */
    public boolean isRunning(){
        return (curRunState == runState.RUNNING);
    }

    /**
     * @return <CODE>true</CODE> if this task has finished.
     */
    public boolean hasFinished(){
        return (curRunState == runState.FINISHED);
    }

    /**
     * Allows the task to be run from start to finish in a single tick.
     * <p>
     * May be overridden by super-classes that do not support single-tick
     * completion.
     */
    public void run() {
        start();
        doAction();
        end();
    }

}
