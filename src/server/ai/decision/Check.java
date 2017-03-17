package server.ai.decision;

import server.ai.Intel;
import server.game.*;

import java.util.ArrayList;
import java.util.HashMap;
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
    private Intel intel;
    private final boolean orbCheck;

    /**
     * Constructs a check object that utilises a given Intel object.
     * @param intel - The Intel object this checker will utilise.
     */
    public Check(Intel intel){
        this.intel = intel;
        this.orbCheck = intel instanceof OrbIntel;
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
        else if (mode == CheckMode.CLOSEST_ENEMY) {
            return closestEnemyCheck();
        }
        else return false;
    }

    private boolean lowHealthCheck(){
        return intel.ent().getHealth() <= LOW_HEALTH_THRESHOLD;
    }

    private boolean orbNearbyCheck(){
        Player me = (Player) intel.ent();

        // Check for orbs in the current phase first.
        ConcurrentHashMap<Integer, Orb> threats =
                ((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(me.getPos().toPoint(), me.getPhase(), 75);
        if (threats != null){
            targetNearestThreat(threats);
            ((PlayerIntel)intel).setPhaseShiftReq(false);
            return true;
        }

        // Then check for orbs in the other phase.
        threats = ((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(me.getPos().toPoint(), 1 - me.getPhase(), 50);
        if (threats != null){
            targetNearestThreat(threats);
            ((PlayerIntel)intel).setPhaseShiftReq(true);
            return true;
        }
        ((PlayerIntel)intel).setPhaseShiftReq(false);
        return false;
    }

    private boolean proximityCheck(){
        ConcurrentHashMap<Integer, Player> playersInSight = intel.getEnemyPlayersInSight(orbCheck);
        if (playersInSight.size() > 0){
            targetNearestThreat(playersInSight);
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
        if (distance > accErr) {
            intel.setTargetLocation(playerAt);
            return true;
        }
        return false;
    }

    private boolean closestEnemyCheck(){
        MovableEntity closestEnt = null;
        float closestDist = -1;

        ConcurrentHashMap<Integer, Player> risks1 = intel.getEnemyPlayersInSight(orbCheck);
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
     * For efficiency purposes, targets the nearest threat from a given collection
     * and stores the entity in the intel object.
     * @param threats - the collection of threats to be range-checked.
     */
    private <E extends MovableEntity> void targetNearestThreat(ConcurrentHashMap<Integer, E> threats){
        float closestDistance = -1;
        MovableEntity closestThreat = null;
        for (java.util.Map.Entry<Integer, E> p : threats.entrySet()){
            float thisDistance = intel.ent().getPos().getDistanceTo(p.getValue().getPos());
            if (closestDistance < 0 || thisDistance < closestDistance){
                closestDistance = thisDistance;
                closestThreat = p.getValue();
            }
        }
        System.out.println("Targeted Threat " + closestThreat.getID());
        intel.setRelevantEntity(closestThreat);
    }
}
