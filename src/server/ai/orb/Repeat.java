package server.ai.orb;

import server.ai.Behaviour;
import server.ai.Intel;
import server.game.Orb;

/**
 * Created by rhys on 2/15/17.
 */
public class Repeat extends Behaviour {

    private final Behaviour behaviour;
    private int iterRem;
    private int iterTotal;

    public Repeat(Behaviour behaviour) {
        super();
        this.behaviour = behaviour;
        this.iterRem = -1;
        this.iterTotal = iterRem;
    }

    public Repeat(Behaviour behaviour, int iterations) {
        super();
        if (iterations < 1) {
            throw new RuntimeException("Can't repeat negative times.");
        }
        this.behaviour = behaviour;
        this.iterTotal = iterations;
        this.iterRem = iterations;
    }

    public void start() {
        super.start();
        this.behaviour.start();
    }

    @Override
    public void reset() {
        this.iterRem = iterTotal;

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
