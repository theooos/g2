package server.ai;

import server.game.Game;
import server.game.Orb;

/**
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
        this.state = BehaviourState.Running;
    }

    public abstract void reset();

    /**
     * Sets in motion the appropriate actions if the starting conditions are met.
     * @param orb - the Orb that this Behaviour will act upon.
     * @param env - the game-state that this Behaviour will use to determine the actions to take.
     */
    public abstract void act(Orb orb, Game env);

    /**
     * Sets a flag to signal that this Behaviour has succeeded.
     */
    protected void succeed(){
        this.state = BehaviourState.Success;
    }

    /**
     * Sets a flag to signal that this Behaviour has failed.
     */
    protected void fail() {
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
