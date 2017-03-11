package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;

/**
 * Created by rhys on 2/16/17.
 */
public abstract class Attack extends Task {

    public Attack(Intel intel, AIBrain brain){
        super(intel, brain);
    }

}
