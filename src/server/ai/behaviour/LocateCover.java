package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.game.Vector2;

import java.util.Random;

/**
 * Allows the entity to attempt to find a safe place to travel to.
 * Created by Rhys on 2/16/17.
 */
public class LocateCover extends Task {

    private Random gen;

    public LocateCover(Intel intel, AIBrain brain) {
        super(intel, brain);
        gen = new Random();
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void doAction(){

        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(false);
        Vector2 newPos = null;
        boolean success = false;

        while (!success) {
            System.out.println("Trying...");
            float ranX = (float) gen.nextInt(intel.getMap().getMapWidth());
            float ranY = (float) gen.nextInt(intel.getMap().getMapLength());
            newPos = new Vector2(ranX, ranY);

            // Check if space is valid.
            success = intel.isValidSpace(newPos) && !intel.inSight(newPos);
        }

        intel.setTargetLocation(newPos);
        end();
    }
}