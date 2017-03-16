package server.ai.decision;

import server.ai.Intel;
import server.game.MovableEntity;
import server.game.Orb;
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
        TARGET_MOVED,
        CLOSEST_ENEMY}

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
            System.out.println("Target Moved: " + targetMovedCheck());
            return targetMovedCheck();
        }
        else if (mode == CheckMode.CLOSEST_ENEMY) {
            return closestEnemyCheck();
        }
        else return false;
    }

    private boolean lowHealthCheck(){
        return intel.ent().getHealth() <= LOW_HEALTH_THRESHOLD;
    }

    private boolean orbNearbyCheck(){
        return false;
        //if (((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(intel.ent().getPos(), intel.ge) )
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
        Vector2 targetPos = intel.getRelevantEntity().getPos();
        Vector2 currentPos = intel.ent().getPos();
        float distance = currentPos.getDistanceTo(targetPos);
        float range = intel.ent().getRadius() + intel.getRelevantEntity().getRadius();
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
        Vector2 playerAt = intel.getRelevantEntity().getPos();
        float distance = targetPos.getDistanceTo(playerAt);
        float accErr = intel.ent().getRadius();
        return (distance > accErr);
    }

    private boolean closestEnemyCheck(){
        MovableEntity closestEnt = null;
        float closestDist = -1;

        ConcurrentHashMap<Integer, Player> risks1 = intel.getEnemyPlayersInSight();
        for (java.util.Map.Entry<Integer, Player> e : risks1.entrySet()){
            float distance = intel.ent().getPos().getDistanceTo(e.getValue().getPos());
            if (closestDist < 0 || distance < closestDist){
                closestDist = distance;
                closestEnt = e.getValue();
            }
        }

        ConcurrentHashMap<Integer, Orb> risks2 = ((PlayerIntel)(intel)).getOrbsInSight();
        for (java.util.Map.Entry<Integer, Orb> e : risks2.entrySet()){
            float distance = intel.ent().getPos().getDistanceTo(e.getValue().getPos());
            if (closestDist < 0 || distance < closestDist){
                closestDist = distance;
                closestEnt = e.getValue();
            }
        }

        if (closestDist == -1 || closestEnt == null){
            return false;
        }
        intel.setRelevantEntity(closestEnt);
        return true;
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
        intel.setRelevantEntity(closestPlayer);
    }
}
