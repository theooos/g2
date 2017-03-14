package server.ai;

import server.ai.pathfinding.AStar;
import server.ai.vision.VisibilityPolygon;
import server.ai.vision.Visualiser;
import server.game.*;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A packet of the necessary data that AI units need to make decisions.
 * Created by Rhys on 2/15/17.
 */
public abstract class Intel {

    protected MovableEntity ent;
    protected ConcurrentHashMap<Integer, Player> players;  // A list of all the players in the game.
    protected HashMap<Integer, Orb> allOrbs;
    protected Map map;                    // The map the current game is being played on.
    protected Vector2 targetLocation;     // Where the entity is currently aiming to reach.
    protected Player targetPlayer;        // The player the entity is currently hunting.
    protected Visualiser sight;
    protected ArrayList<Vector2> path;    // A sequence of points through which the entity
                                        // will travel to reach its target location.
    protected CollisionManager collisionManager;
    protected AStar pathfinder;

    /**
     * Constructs an intel object based on the given Players and Map.
     * @param players - The list of players.
     * @param map - The map currently in play.
     */
    public Intel(ConcurrentHashMap<Integer, Player> players, Map map) {
        this.players = players;
        this.map = map;
        this.targetLocation = null;
        this.targetPlayer = null;
        this.path = new ArrayList<>();
        this.pathfinder = new AStar(this);
    }

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
     * @param pid - The ID of the desired player.
     * @return the requested Player.
     */
    public Player getPlayer(int pid){
        return players.get(pid);
    }

    /**
     * Refreshes this Intel with a given list of Players.
     * @param players - The list of players to be used to refresh the Intel object.
     */
    public void resetPlayers(ConcurrentHashMap<Integer, Player> players) {
        this.players = players;
    }


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
     * @param map - The desired map for refresh.
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
     * @param targetLocation - The new target location.
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
     * @return the next checkpoint to move towards.
     */
    public Vector2 nextCheckpoint(){
        path.remove(0);
        return path.get(0);
    }

    /**
     * @return true if the current checkpoint is the destination.
     */
    public boolean isFinalDestination(){
        return path.size() == 1;
    }

    /**
     * Clears the path and puts a new one in place.
     * @param newPath - The new list of points for the new path.
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
    public Player getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * Stores the player the entity is intending to hunt next.
     * @param targetPlayer - the next target player.
     */
    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }


    public void initForGame(MovableEntity ent, HashMap<Integer, Orb> orbs) {
        this.ent = ent;
        this.allOrbs = orbs;
        constructVisualiser();
        collisionManager = new CollisionManager(players, orbs, map);
    }

    protected abstract void constructVisualiser();

    public ConcurrentHashMap<Integer, Player> getEnemyPlayersInSight(){
        return sight.getPlayersInSight(ent.getPos().toPoint(), ent.getPhase());
    }

    public AStar pathfinder(){
        return pathfinder;
    }
}
