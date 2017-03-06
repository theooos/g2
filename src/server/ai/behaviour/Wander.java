package server.ai.behaviour;

import server.ai.Intel;
import server.ai.OrbBrain;
import server.game.Vector2;

import java.util.Random;

/**
 * Chooses a random worthwhile location for the entity to move towards.
 * Created by rhys on 2/16/17.
 */
public class Wander extends Task {

    public Wander(Intel intel, OrbBrain brain) {
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }


    @Override
    public void doAction() {
        Random gen = new Random();
        float ranX = (float) gen.nextInt(intel.getMap().getMapWidth());
        float ranY = (float) gen.nextInt(intel.getMap().getMapLength());
        intel.setTargetLocation(new Vector2(ranX, ranY));
        end();
    }


}
