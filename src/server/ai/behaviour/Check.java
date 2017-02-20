package server.ai.behaviour;

import server.ai.Intel;
import server.game.Vector2;

/**
 * Can perform a variety of checks on the entity or its environment.
 * Created by rhys on 2/16/17.
 */
public class Check {

    public enum CheckMode { HEALTH, PROXIMITY, RANGE, TARGET_MOVED }
    private Intel intel;

    public Check(Intel intel){
        this.intel = intel;
    }

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
            targetNearestPlayer();
            return true;
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

    private void targetNearestPlayer(){
        intel.setTargetPlayer(intel.getPlayer(0));
    }


}
