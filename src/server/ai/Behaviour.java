package server.ai;

import server.game.Game;
import server.game.Orb;

/**
 * Abstract Behaviour object allowing construction of Behaviour tree.
 * Based on tutorial at https://goo.gl/0f9F0U .
 * Created by rhys on 2/14/17.
 */
public abstract class Behaviour {

    public enum BehaviourState { Success, Failure, Running }
    protected BehaviourState state;

    /**
     * Empty constructor for Abstract class.
     */
    protected Behaviour(){}

    /**
     * Sets a flag to signal that this Behaviour is running.
     */
    public void start() {
        System.out.println(">>> Starting routine: " + this.getClass().getSimpleName());
        this.state = BehaviourState.Running;
    }

    public abstract void reset();

    /**
     * Sets in motion the appropriate actions if the starting conditions are met.
     * @param orb - the Orb that this Behaviour will act upon.
     * @param env - the game-state that this Behaviour will use to determine the actions to take.
     */
    public abstract void act(Orb orb, Intel env);

    /**
     * Sets a flag to signal that this Behaviour has succeeded.
     */
    protected void succeed(){
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " SUCCEEDED");
        this.state = BehaviourState.Success;
    }

    /**
     * Sets a flag to signal that this Behaviour has failed.
     */
    protected void fail() {
        System.out.println(">>> Routine: " + this.getClass().getSimpleName() + " FAILED");
        this.state = BehaviourState.Failure;
    }

    /**
     * @return true if this Behaviour is currently running.
     */
    public boolean isRunning() {
        return state.equals(BehaviourState.Running);
    }

    /**
     * @return true if this Behaviour has succeeded.
     */
    public boolean isSuccess() {
        return state.equals(BehaviourState.Success);
    }

    /**
     * @return true if this Behaviour has failed.
     */
    public boolean isFailure() {
        return state.equals(BehaviourState.Failure);
    }

    /**
     * @return which state this Behaviour is currently in.
     */
    public BehaviourState getState() {
        return state;
    }
}
