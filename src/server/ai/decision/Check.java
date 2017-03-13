package server.ai.decision;

import server.ai.Intel;
import server.ai.vision.VisibilityPolygon;
import server.game.Player;
import server.game.Vector2;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Can perform a variety of checks on the entity or its environment.
 * Created by rhys on 2/16/17.
 */
public class Check {

    public enum CheckMode {
        HEALTH,
        PROXIMITY,
        RANGE,
        COUNTER_ATTACK_VIABLE,
        TARGET_MOVED }
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
            int curHealth = ((PlayerIntel)(intel)).ent().getHealth();
            boolean healthDown = (((PlayerIntel)(intel)).healthLastTick() > curHealth);
            if (healthDown) ((PlayerIntel)(intel)).rememberHealth(curHealth);
            return healthDown;
        }

        // Returns true if there is an enemy player within the entity's field of vision.
        else if (mode == CheckMode.PROXIMITY) {
            ConcurrentHashMap<Integer, Player> playersInSight = intel.getEnemyPlayersInSight();
            if (playersInSight.size() > 0){
                targetNearestPlayer(playersInSight);
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
     * For efficiency purposes, targets the nearest enemy player upon detection and
     * stores the player in the intel object.
     * @param playersInSight - the enemy players within visible range.
     */
    private void targetNearestPlayer(ConcurrentHashMap<Integer, Player> playersInSight){
        float closestDistance = -1;
        Player closestPlayer = null;
        for (java.util.Map.Entry<Integer, Player> p : playersInSight.entrySet()){
            float thisDistance = intel.ent().getPos().getDistanceTo(p.getValue().getPos());
            if (closestDistance < 0 || thisDistance < closestDistance){
                closestDistance = thisDistance;
                closestPlayer = p.getValue();
            }
        }
        intel.setTargetPlayer(closestPlayer);
    }
}