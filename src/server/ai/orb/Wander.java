package server.ai.orb;

import server.ai.Behaviour;
import server.ai.Intel;
import server.game.Orb;
import server.game.Vector2;

import java.util.Random;

/**
 * Created by rhys on 2/15/17.
 */
public class Wander extends Behaviour {

    private static Random gen;
    private final Intel env;
    private Stalk stalk;

    public Wander(Intel env){
        super();
        this.env = env;
        this.gen = new Random();
        this.stalk = new Stalk(genRandomVector());
    }

    @Override
    public void start() {
        super.start();
        this.stalk.start();
    }

    @Override
    public void reset() {
        this.stalk = new Stalk(genRandomVector());
    }

    @Override
    public void act(Orb orb, Intel env) {
        if (!stalk.isRunning()) {
            return;
        }
        this.stalk.act(orb, env);
        if (this.stalk.isSuccess()) {
            succeed();
        } else if (this.stalk.isFailure()) {
            fail();
        }
    }

    private Vector2 genRandomVector(){
        float ranX = (float) gen.nextInt(env.getMap().getMapWidth());
        float ranY = (float) gen.nextInt(env.getMap().getMapLength());
        return new Vector2(ranX, ranY);

    }
}
