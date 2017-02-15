package server.ai;

import server.ai.Behaviour;
import server.ai.Intel;
import server.game.MovableEntity;
import server.game.Orb;

/**
 * Created by rhys on 2/15/17.
 */
public class Live extends Behaviour {

    private Feel feel;

    public Live(Intel env, MovableEntity e) {
        super();
        feel = new Feel();

    }

    public void start() {
        super.start();
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Orb orb, Intel env) {
        if (behaviour.isFailure()) {
            fail();
        } else if (behaviour.isSuccess()) {
            if (iterRem == 0) {
                succeed();
                return;
            }
            if (iterRem > 0 || iterRem <= -1) {
                iterRem--;
                behaviour.reset();
                behaviour.start();
            }
        }
        if (behaviour.isRunning()) {
            behaviour.act(orb, env);
        }
    }


}
