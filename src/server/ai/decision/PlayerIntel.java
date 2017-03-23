package server.ai.decision;

import server.ai.Intel;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Extends the Intel object to contain information specifically vital to AI-controlled players.
 * Created by Rhys on 3/11/17.
 */
public class PlayerIntel extends Intel {

    private boolean escaped;
    private boolean phaseShiftFailed;
    private boolean phaseShiftReq;
    private MovableEntity entityBuffer;
    private Vector2 lastCursorPos;

    /**
     * Constructs a players intelligence object that tracks the given players and power-ups
     * and holds information about the given game-map.
     * @param players the players involved in the current game.
     * @param map     the map the current game is being played on.
     * @param pUps    the power-ups in use in this game.
     */
    public PlayerIntel(ConcurrentHashMap<Integer, Player> players, Map map, HashMap<Integer, PowerUp> pUps){
        super(players, map, pUps);
        this.phaseShiftFailed = false;
        this.lastCursorPos = new Vector2(map.getMapWidth()/2, map.getMapLength()/2);
    }

    /**
     * @return this intelligence object's parent player.
     */
    @Override
    public AIPlayer ent(){
        return (AIPlayer) ent;
    }

    /**
     * Checks the player's current position against the game's collision
     * manager for position validity.
     *
     * @return <CODE>true</CODE> if the player's position within the game is valid.
     */
    public boolean validPosition(){
        return collisionManager.validPosition(ent);
    }

    /**
     * @return the intelligence object's line-of-sight checker.
     */
    public Visualiser getVisualiser(){
        return sight;
    }

    /**
     * Sets a flag to show future methods that an attempt to phase-shift failed.
     */
    public void failedPhaseShift(){
        this.phaseShiftFailed = true;
    }

    /**
     * Checks whether or not a phase-shift has failed recently and resets the flag.
     *
     * @return <CODE>true</CODE> if a recent phase-shift attempt failed.
     */
    public boolean phaseShiftFailed(){
        boolean r = phaseShiftFailed;
        phaseShiftFailed = false;
        return r;
    }

    /**
     * Calls on the {@link Visualiser} object to check which Orbs are currently
     * within line-of-sight of the player.
     *
     * @return a collection of Orbs whom the player can see from their current position.
     */
    public ConcurrentHashMap<Integer, Orb> getOrbsInSight(){
        return sight.getOrbsInSight(ent.getPos().toPoint(), ent.getPhase(), 0);
    }

    /**
     * Returns the result of a flag that tells whether or not a phase-shift is (or was)
     * required in order to carry out a scheduled behaviour.
     *
     * @return <CODE>true</CODE> if a phase-shift is/was required for the current behaviour.
     */
    public boolean isPhaseShiftReq(){
        return phaseShiftReq;
    }

    /**
     * Sets a flag to say whether or not a phase-shift is required to carry out
     * or return from an upcoming behaviour.
     *
     * @param req should be set to true if a phase-shift is required and false
     *            otherwise.
     */
    public void setPhaseShiftReq(boolean req){
        this.phaseShiftReq = req;
    }

    /**
     * Nullifies and re-initialises several overloaded members to prevent
     * leakages between behaviours belonging to different emotional processes.
     */
    @Override
    public void resetIntel(){
        super.resetIntel();
        this.escaped = false;
        this.phaseShiftFailed = false;
        this.phaseShiftReq = false;
    }

    /**
     * Sets the entity that the parent player is currently interested in, whether it
     * is an enemy player, orb or power-up.
     *
     * @param relEnt the entity relevant to upcoming behaviours.
     */
    @Override
    public void setRelevantEntity(MovableEntity relEnt){
        this.entityBuffer = relEnt;
    }

    /**
     * Loads the relevant entity from a safe buffer member that doesn't get overwritten
     * by {@link #resetIntel()}, allowing the player's reactions to be delayed without
     * forgetting which entity they are currently interested in.
     */
    public void loadRelevantEntity(){
        this.relevantEnt = this.entityBuffer;
    }

    /**
     * Calculates the direction vector towards where the AI player's virtual cursor is.
     *
     * @return a direction vector that causes rendering of the AI player to point towards
     *         their virtual cursor.
     */
    public Vector2 getPointerVector(){
        return ent.getPos().vectorTowards(lastCursorPos).normalise();
    }

    /**
     * Sets the AI player's virtual cursor at the most recent position the AI aimed towards.
     * Storing this facilitates the rendering of an AI player while travelling to look more
     * similar to a human-controlled player, as human-controlled players don't change their
     * pointer direction every time they move.
     *
     * @param thisPos the position of the virtual cursor.
     */
    public void setLastCursorPos(Vector2 thisPos){
        this.lastCursorPos = thisPos;
    }

    /**
     * Forces the relevant entity to be stored in the {@link #relevantEnt} member straight
     * away, skipping the buffer, so that the player can track data about it immediately.
     *
     * @param relEnt the entity relevant to the player's next behaviour.
     */
    public void forceRelevantEntity(MovableEntity relEnt) {
        this.relevantEnt = relEnt;
    }
}
