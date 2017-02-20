package server.ai.behaviour;

import server.ai.Intel;
import server.ai.OrbBrain;

/**
 * Created by rhys on 2/16/17.
 */
public abstract class Attack extends Task {

    public Attack(Intel intel, OrbBrain brain){
        super(intel, brain);
    }

}
