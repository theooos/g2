package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.OrbBrain;
import server.game.Entity;
import server.game.Game;
import server.game.MovableEntity;
import server.game.Vector2;

import java.util.Random;

/**
 * Allows the entity to follow a path.
 * Created by rhys on 2/16/17.
 */
public class Travel extends Task {

    public Travel(Intel intel, AIBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (intel.ent().isAlive() &&
                intel.getTargetLocation() != null &&
                intel.checkpoint() != null);
    }

    @Override
    public void doAction() {

        MovableEntity ent = intel.ent();
        // Sets the entity to face in direction of the target location.
        Vector2 target = ent.getPos().vectorTowards(intel.checkpoint())/*.normalise()*/;
        ent.setDir(deviate(target));

        // Checks for collisions,
        Vector2 newLoc = ent.getPos().add(ent.getDir().mult(ent.getSpeed()));
        if (collision(newLoc)) {
            end();
        } else {
            // Moves the entity.
            ent.setPos(newLoc);

            // Check if the checkpoint has been reached.
            boolean reached = intel.checkpoint().getDistanceTo(ent.getPos())
                    <= ent.getRadius();

            // Update the entity's state for the next tick.
            if (reached && intel.isFinalDestination()){
                end();
            } else if (reached) {
                intel.nextCheckpoint();
            }
        }
    }

    @Override
    public void run(){
        System.err.println("Travel is not a single-tick task.");
        System.exit(1);
    }

    private Vector2 deviate(Vector2 targetDirection){

        Random gen = new Random();
        double ang = Math.atan(targetDirection.getX()/targetDirection.getY());
        if (Double.isInfinite(ang)) {
            ang = 0;
        } else if (targetDirection.getY() < 0) {
            ang += Math.PI;
        }
        ang += Math.toRadians(gen.nextInt(30));
        float newX = (float)(Math.sin(ang));
        float newY = (float)(Math.cos(ang));

        return (new Vector2(newX, newY)).normalise();
    }

    private boolean collision(Vector2 newLoc){
        int r = intel.ent().getRadius();

        Entity obstruction = Game.collidesWithBot(r, newLoc, intel.getOrbs());
        boolean collidedWithOrb = !(obstruction.equals(null)) && !(obstruction.getID() == intel.ent().getID());
        if (collidedWithOrb){
            return true;
        }

        boolean collidedWithWall = Game.pointWallCollision(r, newLoc, intel.ent().getPhase(), intel.getMap());

        return collidedWithWall;
    }


}
