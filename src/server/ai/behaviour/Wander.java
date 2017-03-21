package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.game.Vector2;

import java.util.Random;

/**
 * This behaviour allows AI-controlled units to choose a random, out-of-sight
 * location to travel towards.
 *
 * Created by Rhys on 2/16/17.
 */
public class Wander extends Task {

    private Random gen;

    /**
     * Constructs a Wander behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     */
    public Wander(Intel intel, AIBrain brain) {
        super(intel, brain);
        this.gen = new Random();
    }

    @Override
    public boolean checkConditions() {
        return intel.ent().isAlive();
    }

    @Override
    public void doAction() {

        //  System.out.println("Finding location to wander to.");
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(false);
        Vector2 newPos = null;
        boolean success = false;

        while (!success) {
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