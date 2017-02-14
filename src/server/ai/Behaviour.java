package server.ai;

import server.game.Game;
import server.game.Zombie;

/**
 * Created by rhys on 2/14/17.
 */
public abstract class Behaviour {

    public enum BehaviourState { Success, Failure, Running }
    protected BehaviourState state;

    protected Behaviour(){}

    public void start() {
        this.state = BehaviourState.Running;
    }

    public abstract void reset();

    public abstract void act(Zombie orb, Game env);

    protected void succeed(){
        this.state = BehaviourState.Success;
    }

    protected void fail() {
        this.state = BehaviourState.Failure;
    }

    public boolean isSRunning() {
        return state.equals(BehaviourState.Running);
    }

    public boolean isSuccess() {
        return state.equals(BehaviourState.Success);
    }

    public boolean isFailure() {
        return state.equals(BehaviourState.Failure);
    }

    public BehaviourState getStae() {
        return state;
    }
}
