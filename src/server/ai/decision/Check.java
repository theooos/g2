package server.ai.decision;

import server.ai.Intel;
import server.game.*;

import java.util.concurrent.ConcurrentHashMap;

import static server.ai.decision.AIConstants.LOW_HEALTH_THRESHOLD;

/**
 * Can perform a variety of checks on an AI-controlled entity or its environment.
 *
 * Created by Rhys on 2/16/17.
 */
public class Check {

    public enum CheckMode {
        LOW_HEALTH,
        ORB_NEARBY,
        PROXIMITY,
        RANGE,
        TARGET_MOVED,
        CLOSEST_ENEMY,
        ENEMY_IN_PHASE,
        HEALTH_UP_VIABLE
    }

    private Intel intel;
    private final boolean orbCheck;

    /**
     * Constructs a check object that utilises a given Intel object.
     *
     * @param intel the Intel object this checker will utilise.
     */
    public Check(Intel intel){
        this.intel = intel;
        this.orbCheck = intel instanceof OrbIntel;
    }

    /**
     * Performs the requested check.
     *
     * @param mode the check to be carried out.
     * @return     true if the check passes.
     */
    public boolean doCheck(CheckMode mode) {

        switch (mode) {

            case LOW_HEALTH:
                return lowHealthCheck();

            case ORB_NEARBY:
                return orbNearbyCheck();

            case PROXIMITY:
                return proximityCheck();

            case RANGE:
                return rangeCheck();

            case TARGET_MOVED:
                return targetMovedCheck();

            case CLOSEST_ENEMY:
                return closestEnemyCheck();

            case HEALTH_UP_VIABLE:
                return healthcareAvailableCheck();

            case ENEMY_IN_PHASE:
                return enemyInPhaseCheck();

            default:
                return false;
        }
    }

    /**
     * Checks the entity's current health-level against the defined threshold.
     *
     * @return true if health is below threshold.
     */
    private boolean lowHealthCheck(){
        return intel.ent().getHealth() <= LOW_HEALTH_THRESHOLD;
    }

    /**
     * Checks in both phases for nearby orbs, and sets the nearest one as the target if
     * one is near - prioritising orbs in the entity's current phase. Check radius is greater in
     * the current phase to simulate how a human would play.
     * <p>
     * If an orb is found, this method will set a boolean flag in the player's intel object
     * for later methods to know which phase to look in.
     *
     * @return true if an orb is in sight and within range, in either phase.
     */
    private boolean orbNearbyCheck(){

        Player me = (Player) intel.ent();

        // Check for orbs in the current phase first.
        ConcurrentHashMap<Integer, Orb> threats =
                ((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(me.getPos().toPoint(),
                        me.getPhase(), 150);
        if (threats.size() > 0){
            targetNearestThreat(threats);
            ((PlayerIntel)intel).setPhaseShiftReq(false);
            return true;
        }

        // Then check for orbs in the other phase.
        threats = ((PlayerIntel)(intel)).getVisualiser().getOrbsInSight(me.getPos().toPoint(),
                1 - me.getPhase(), 50);
        if (threats.size() > 0){
            targetNearestThreat(threats);
            ((PlayerIntel)intel).setPhaseShiftReq(true);
            return true;
        }
        else {
            ((PlayerIntel)intel).setPhaseShiftReq(false);
            return false;
        }

    }

    /**
     * Checks if there is a player in sight in either phase, and sets the closest detected player
     * as the target upon detection.
     *
     * @return true if there is a player in line-of-sight, in either phase.
     */
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

    /**
     * Checks whether a targeted player is within range of an Orb's zap-attack.
     *
     * @return true if the orb is able to zap the target player from their current
     * position.
     */
    private boolean rangeCheck(){
        Vector2 targetPos = intel.getRelevantEntity().getPos();
        Vector2 currentPos = intel.ent().getPos();
        float distance = currentPos.getDistanceTo(targetPos);
        float range = intel.ent().getRadius() + intel.getRelevantEntity().getRadius();
        return (distance <= range);
    }

    /**
     * Checks whether or not a target has moved a greater distance than their radius
     * since the last time their position was updated.
     *
     * @return true if the target has moved.
     */
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

    /**
     * Sets the closest threat as the relevant entity, under the assumption that
     * the fact that it's the closest means it's the greatest threat to the player
     * at times of low health.
     *
     * @return true if a threat is found anywhere within line of sight.
     */
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
     * Finds the nearest threat from a given collection and stores it as the target
     * in the intel object.
     *
     * @param threats the collection of threats to be range-checked.
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
        intel.setRelevantEntity(closestThreat);

    }

    /**
     * Checks if there is health power-up currently active in the game and sets
     * it as the target if found. Also sets a boolean flag in the player's intel object
     * so that future methods know which phase to find the power-up in.
     *
     * @return true if there is an active health power-up in either phase.
     */
    private boolean healthcareAvailableCheck(){
        boolean found = false;
        for (java.util.Map.Entry<Integer, PowerUp> e : intel.getPowerUps().entrySet()){
            boolean inPhase = e.getValue().getPhase() == intel.ent().getPhase();
            if (e.getValue().getType() == PowerUp.Type.health && e.getValue().isAlive()){
                if (inPhase) {
                    ((PlayerIntel)intel).setPhaseShiftReq(false);
                }
                else {
                    ((PlayerIntel)intel).setPhaseShiftReq(true);
                }
                found = true;
                ((PlayerIntel)intel).forceRelevantEntity(e.getValue());
                break;
            }
        }
        return found;
    }

    /**
     * Checks if there are any enemy players within the entity's current phase -
     * regardless of whether a direct line-of-sight can be established - and sets
     * the closest as the target.
     *
     * @return true if there is an enemy player in this phase.
     */
    private boolean enemyInPhaseCheck(){
        ConcurrentHashMap<Integer, Player> relPlayers = new ConcurrentHashMap<>();
        for (java.util.Map.Entry<Integer, Player> e : intel.getPlayers().entrySet()){
            if (e.getValue().getPhase() == intel.ent().getPhase()
                    && e.getValue().getTeam() != intel.ent().getTeam()
                    && e.getValue().isAlive()) {
                relPlayers.put(e.getKey(), e.getValue());
            }
        }
        if (relPlayers.size() > 0){
            targetNearestThreat(relPlayers);
            return true;
        }
        else {
            return false;
        }
    }
}
