package server.ai.orb;

import server.ai.Behaviour;
import server.ai.Intel;
import server.game.Map;
import server.game.Orb;

/**
 * Created by rhys on 2/15/17.
 */
public class Feel extends Behaviour {

    private Intel env;
    private Map map;

    public Feel(Intel env, Map map){
        this.env = env;
        this.map = map;
    }


    @Override
    public void reset() {

    }

    @Override
    public void act(Orb orb, Intel env) {

    }
}
