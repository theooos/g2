package server.ai.behaviour;

import server.ai.Intel;

/**
 * Created by rhys on 2/16/17.
 */
public abstract class Attack extends Task {

    public Attack(Intel intel){
        super(intel);
    }

    @Override
    public void start(){
        this.curRunState = runState.RUNNING;
    }

}
