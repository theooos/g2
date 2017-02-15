package server.ai.orb;

import server.ai.Behaviour;
import server.ai.Intel;
import server.game.Orb;
import server.game.Vector2;

/**
 * Allows an orb to move towards a targeted location.
 * Created by rhys on 2/14/17.
 */
public class Stalk extends Behaviour {

    protected Vector2 target;

    public Stalk(Vector2 target){
        super();
        this.target = target;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Orb orb, Intel env) {
        if (isRunning()) {
            // Fail if the orb has died.
            if (!orb.isAlive()) {
                fail();
                return;
            }

            // Check if there is a better target nearby.


            if (!reachedTarget(orb)){
                moveOrb(orb);
            }
        }
    }

    /**
     * Moves the Orb one step towards the target location.
     * @param orb - The Orb to be moved.
     */
    private void moveOrb(Orb orb){
        // Sets the Orb to face in direction of the target location.
        orb.setDir((orb.getPos().vectorTowards(target)).normalise());

        // Moves the Orb.
        orb.setPos(orb.getPos().add(orb.getDir().mult(orb.getSpeed())));

        if (reachedTarget(orb)){
            succeed();
        }
    }

    /**
     * Checks whether or not the target location has been reached.
     * @param orb - The Orb whose location is to be checked.
     * @return true if the Orb is touching the target location.
     */
    private boolean reachedTarget(Orb orb){
       // System.out.println("Distance: " + target.getDistanceTo(orb.getPos()));
        return target.getDistanceTo(orb.getPos()) <= orb.getRadius();
    }
}
