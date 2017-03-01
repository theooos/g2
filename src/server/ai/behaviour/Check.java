package server.ai.behaviour;

import server.ai.Intel;
import server.ai.vision.VisibilityPolygon;
import server.game.Player;
import server.game.Vector2;

/**
 * Can perform a variety of checks on the entity or its environment.
 * Created by rhys on 2/16/17.
 */
public class Check {

    public enum CheckMode { HEALTH, PROXIMITY, RANGE, TARGET_MOVED }
    private Intel intel;        // The intelligence this object uses to perform its checks.

    /**
     * Constructs a check object that utilises a given Intel object.
     * @param intel - The Intel object this checker will utilise.
     */
    public Check(Intel intel){
        this.intel = intel;
    }

    /**
     * Performs the requested check.
     * @param mode - The check to be carried out.
     * @return true if the check passes.
     */
    public boolean doCheck(CheckMode mode) {

        // Returns true if the entity has lost health since the last tick.
        if (mode == CheckMode.HEALTH) {
            int curHealth = intel.ent().getHealth();
            boolean healthDown = (intel.healthLastTick() > curHealth);
            if (healthDown) intel.rememberHealth(curHealth);
            return healthDown;
        }

        // SKELETON: Returns true if there is a player within the entity's field of vision.
        else if (mode == CheckMode.PROXIMITY) {
            if (playerInSight()){
                targetNearestPlayer();
                return true;
            }
            else {
                return false;
            }
        }

        // Returns true if the Orb is in attacking range of the targeted player.
        else if (mode == CheckMode.RANGE) {
            Vector2 targetPos = intel.getTargetPlayer().getPos();
            Vector2 currentPos = intel.ent().getPos();
            float distance = currentPos.getDistanceTo(targetPos);
            float range = intel.ent().getRadius() + intel.getTargetPlayer().getRadius();
            return (distance <= range);
        }

        // Returns true if the targeted player has moved since the last tick.
        else if (mode == CheckMode.TARGET_MOVED) {
            if (intel.getTargetLocation() == null) {
                return true;
            }
            Vector2 targetPos = intel.getTargetLocation();
            Vector2 playerAt = intel.getTargetPlayer().getPos();
            float distance = targetPos.getDistanceTo(playerAt);
            float accErr = intel.ent().getRadius();
            return (distance > accErr);

        }
        else return false;
    }

    /**
     * For efficiency purposes, targets the nearest player upon detection and
     * stores the player in the intel object.
     */
    private void targetNearestPlayer(){
        // Skeleton function.
        // Perquisites: Collision detection.
        intel.setTargetPlayer(intel.getPlayer(0));
    }

    private boolean playerInSight(){
        VisibilityPolygon sight = intel.updateSight();
        for (Player p : intel.getPlayers()){
            if (sight.contains(p.getPos().toPoint())){
                return true;
            }
        }
        return false;
    }
}
