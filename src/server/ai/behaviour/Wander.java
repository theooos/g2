package server.ai.behaviour;

import server.ai.Intel;
import server.game.MovableEntity;
import server.game.Vector2;

import java.util.Random;

/**
 * Chooses a random worthwhile location for the entity to move towards.
 * Created by rhys on 2/16/17.
 */
public class Wander extends Task {

    public Wander(Intel intel) {
        super(intel);
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void start() {
        System.out.println("Starting Task: WANDER.");
    }

    @Override
    public void end() {
        System.out.println("Ending Task: WANDER.");

    }

    @Override
    public void doAction() {
        Random gen = new Random();
        float ranX = (float) gen.nextInt(intel.getMap().getMapWidth());
        float ranY = (float) gen.nextInt(intel.getMap().getMapLength());
        intel.setTargetLocation(new Vector2(ranX, ranY));
        getControl().succeed();
    }


}
