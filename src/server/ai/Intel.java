package server.ai;

import server.ai.decision.Visualiser;
import server.ai.pathfinding.AStar;
import server.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A packet of the necessary data that AI units need to make decisions.
 *
 * Created by Rhys on 2/15/17.
 */
public abstract class Intel {

    protected MovableEntity ent;
    protected ConcurrentHashMap<Integer, Player> players;  // A list of all the players in the game.
    private HashMap<Integer, Orb> allOrbs;
    private HashMap<Integer, PowerUp> powerUps;
    protected Map map;                    // The map the current game is being played on.
    private Vector2 targetLocation;     // Where the entity is currently aiming to reach.
    protected MovableEntity relevantEnt;  // The entity this entity is currently interested in.
    private ArrayList<Vector2> path;    // A sequence of points through which the entity
                                        // will travel to reach its target location.
    protected Visualiser sight;
    protected CollisionManager collisionManager;
    public AStar pathfinder;

    /**
     * Constructs an intelligence object that tracks the given players within
     * the given game map.
     *
     * @param players the list of players.
     * @param map     the map currently in play.
     */
    public Intel(ConcurrentHashMap<Integer, Player> players, Map map, HashMap<Integer, PowerUp> pUps) {
        this.players = players;
        this.powerUps = pUps;
        this.map = map;
        this.targetLocation = null;
        this.relevantEnt = null;
        this.path = new ArrayList<>();
        this.pathfinder = new AStar(this);
    }

    /**
     * @return the owning entity of this intelligence object.
     */
    public MovableEntity ent() {
        return ent;
    }

    /**
     * @return the list of players this Intel object is tracking.
     */
    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * Returns the Player at the specified position in the player list.
     *
     * @param pid the ID of the desired player.
     * @return    the requested Player.
     */
    public Player getPlayer(int pid){
        return players.get(pid);
    }

    /**
     * Refreshes this Intel with a given list of Players.
     *
     * @param players the list of players to be used to refresh the Intel object.
     */
    public void resetPlayers(ConcurrentHashMap<Integer, Player> players) {
        this.players = players;
    }

    /**
     * @return a collection of the orbs that this intelligence object is tracking.
     */
    public HashMap<Integer, Orb> getOrbs(){
        return allOrbs;
    }

    /**
     * @return the map this Intel is currently using.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Re-sets the map that this Intel is to use for the next game.
     *
     * @param map the desired map for refresh.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @return the location the entity is currently aiming to reach.
     */
    public Vector2 getTargetLocation() {
        return targetLocation;
    }

    /**
     * Stores the location the entity is currently aiming to reach.
     *
     * @param targetLocation the new target location.
     */
    public void setTargetLocation(Vector2 targetLocation) {
        this.targetLocation = targetLocation;
    }

    /**
     * @return the position on the path currently moving towards.
     */
    public Vector2 checkpoint(){
        if (path == null) {
            return null;
        } else {
            return path.get(0);
        }
    }

    /**
     * Updates the path to acknowledge a checkpoint being reached.
     *
     * @return the next checkpoint to move towards.
     */
    public Vector2 nextCheckpoint(){
        path.remove(0);
        return path.get(0);
    }

    /**
     * @return <CODE>true</CODE> if the current checkpoint is the destination.
     */
    public boolean isFinalDestination(){
        return path.size() == 1;
    }

    /**
     * Clears the path and puts a new one in place.
     *
     * @param newPath the new list of points for the new path.
     */
    public void resetPath(ArrayList<Vector2> newPath){
        if (this.path != null){
            this.path.clear();
        }
        this.path = newPath;
    }

    /**
     * @return the player the entity is currently hunting.
     */
    public MovableEntity getRelevantEntity() {
        return relevantEnt;
    }

    /**
     * Stores the player the entity is intending to hunt next.
     *
     * @param relEnt the next target player.
     */
    public void setRelevantEntity(MovableEntity relEnt) {
        this.relevantEnt = relEnt;
    }

    /**
     * Prepare this intelligence object for the game by assigning its owner and
     * the other orbs involved in the game.
     *
     * @param ent  the parent entity of this intel object.
     * @param orbs collection of all orbs involved in the upcoming game.
     */
    public void initForGame(MovableEntity ent, HashMap<Integer, Orb> orbs) {
        this.ent = ent;
        this.allOrbs = orbs;
        this.collisionManager = new CollisionManager(players, orbs, map, powerUps);
        this.sight = new Visualiser(map, players, allOrbs, ent.getID());
    }

    /**
     * Checks with the {@link CollisionManager} object whether or not the
     * given position is valid within the current state of the game environment.
     *
     * @param pos the position to be validity-checked.
     * @return    <CODE>true</CODE> if the position is valid.
     */
    public boolean isValidSpace(Vector2 pos){
        return collisionManager.validPosition(pos, ent.getRadius(), ent.getPhase());
    }

    /**
     * Checks with the {@link Visualiser} object which enemy players are in sight.
     *
     * @param orbVision whether or not both phases should be checked, such as for
     *                  use by Orbs.
     * @return          a collection of visible enemy players.
     */
    public ConcurrentHashMap<Integer, Player> getEnemyPlayersInSight(boolean orbVision){
        return sight.getPlayersInSight(ent.getPos().toPoint(), ent.getPhase(), orbVision);
    }

    /**
     * Checks with the {@link Visualiser} object whether or not the given position
     * can be seen from the entity's current position.
     *
     * @param pos the position to be line-of-sight checked.
     * @return    <CODE>true</CODE> if the position is within line-of-sight.
     */
    public boolean inSight(Vector2 pos){
        return sight.inSight(ent.getPos().toPoint(), pos.toPoint(), ent.getPhase());
    }

    /**
     * @return the {@link AStar} object for the entity to use for complex path-finding.
     */
    public AStar pathfinder(){
        return pathfinder;
    }

    /**
     * @return a new {@link CollisionManager} instance.
     */
    public CollisionManager getNewCollisionManager(){
        return new CollisionManager(players, allOrbs, map, powerUps);
    }

    /**
     * Nullifies a few overloaded members so that data doesn't bleed between emotional
     * processes.
     */
    public void resetIntel(){
        this.targetLocation = null;
        this.relevantEnt = null;
        this.path = null;
    }

    /**
     * @return a collection of the power-ups in use in the current game.
     */
    public HashMap<Integer, PowerUp> getPowerUps(){
        return powerUps;
    }
}
