package server.ai.decision;

import server.ai.Intel;
import server.game.Player;
import server.game.Vector2;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Can perform a variety of checks on the entity or its environment.
 * Created by rhys on 2/16/17.
 */
public class Check {

    public enum CheckMode {
        LOW_HEALTH,
        ORB_NEARBY,
        PROXIMITY,
        RANGE,
        RETALIATION_VIABLE,
        TARGET_MOVED }

    private final int LOW_HEALTH_THRESHOLD = 30;
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

        if (mode == CheckMode.LOW_HEALTH) {
            return lowHealthCheck();
        }
        else if (mode == CheckMode.ORB_NEARBY) {
            return orbNearbyCheck();
        }
        else if (mode == CheckMode.PROXIMITY) {
            return proximityCheck();
        }
        else if (mode == CheckMode.RANGE) {
            return rangeCheck();
        }
        else if (mode == CheckMode.RETALIATION_VIABLE){
            return retaliationViableCheck();
        }
        else if (mode == CheckMode.TARGET_MOVED) {
            return targetMovedCheck();
        }
        else return false;
    }

    private boolean lowHealthCheck(){
        return intel.ent().getHealth() <= LOW_HEALTH_THRESHOLD;
    }

    private boolean orbNearbyCheck(){
        if (((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(intel.ent().getPos(), intel.ge) )
    }

    private boolean proximityCheck(){
        ConcurrentHashMap<Integer, Player> playersInSight = intel.getEnemyPlayersInSight();
        if (playersInSight.size() > 0){
            targetNearestPlayer(playersInSight);
            return true;
        }
        else {
            return false;
        }
    }

    private boolean rangeCheck(){
        Vector2 targetPos = intel.getTargetPlayer().getPos();
        Vector2 currentPos = intel.ent().getPos();
        float distance = currentPos.getDistanceTo(targetPos);
        float range = intel.ent().getRadius() + intel.getTargetPlayer().getRadius();
        return (distance <= range);
    }

    private boolean retaliationViableCheck(){
        return false;
    }

    private boolean targetMovedCheck(){
        if (intel.getTargetLocation() == null) {
            return true;
        }
        Vector2 targetPos = intel.getTargetLocation();
        Vector2 playerAt = intel.getTargetPlayer().getPos();
        float distance = targetPos.getDistanceTo(playerAt);
        float accErr = intel.ent().getRadius();
        return (distance > accErr);
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
