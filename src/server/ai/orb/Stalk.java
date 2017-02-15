package server.ai.orb;

import server.ai.Behaviour;
import server.game.Game;
import server.game.Orb;
import server.game.Player;

/**
 * Allows an orb to move towards a targeted player.
 * Created by rhys on 2/14/17.
 */
public class Stalk extends Behaviour {

    protected Player target;

    public Stalk(Player target){
        super();
        this.target = target;
    }

    @Override
    public void reset() {
        start();
    }

    @Override
    public void act(Orb orb, Game env) {
        if (isRunning()) {
            if (!orb.isAlive()) {
                fail();
                return;
            }
            if (!reachedTarget(orb)){
                moveOrb(orb);
            }
        }
    }

    /**
     * Moves the Orb one step towards the target player.
     * @param orb - the Orb to be moved.
     */
    private void moveOrb(Orb orb){
        // Sets the Orb to face in direction of the target player.
        orb.setDir((orb.getPos().vectorTowards(target.getPos())).normalise());
        orb.setPos(orb.getPos().add(orb.getDir().mult(orb.getSpeed())));
    }

    /**
     * Checks whether or not the target player has been reached.
     * @param orb - the Orb whose location is to be checked.
     * @return true if the Orb is touching the target player.
     */
    private boolean reachedTarget(Orb orb){
        System.out.println("Distance: " + target.getPos().getDistanceTo(orb.getPos()));
        return target.getPos().getDistanceTo(orb.getPos()) <= orb.getRadius();
    }
}
