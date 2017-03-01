package server.ai;

import server.ai.vision.VisibilityPolygon;
import server.game.Map;
import server.game.MovableEntity;
import server.game.Player;
import server.game.Vector2;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

/**
 * A packet of the necessary data that AI units need to make decisions.
 * Created by Rhys on 2/15/17.
 */
public class Intel {

    private MovableEntity ent;          // The entity this information is used by.
    private ArrayList<Player> players;  // A list of all the players in the game.
    private Map map;                    // The map the current game is being played on.
    private int healthLastTick;         // The entity's health during the previous tick.
    private Vector2 targetLocation;     // Where the entity is currently aiming to reach.
    private Player targetPlayer;        // The player the entity is currently hunting.
    private VisibilityPolygon sight;    // The entity's field of vision.
    private ArrayList<Vector2> path;    // A sequence of points through which the entity
                                        // will travel to reach its target location.

    /**
     * Constructs an intel object based on the given Players and Map.
     * @param players - The list of players.
     * @param map - The map currently in play.
     */
    public Intel(ArrayList<Player> players, Map map) {
        this.players = players;
        this.map = map;
        this.targetLocation = null;
        this.targetPlayer = null;
        this.path = new ArrayList<>();
    }

    /**
     * Assigns this Intel's owning entity.
     * @param ent - The entity this intel object is to belong to.
     */
    public void assignEntity(MovableEntity ent){
        this.ent = ent;
        this.healthLastTick = ent.getHealth();
        this.sight = new VisibilityPolygon(this.ent.getPhase(), this.map);
    }

    /**
     * @return the entity that owns this intel object.
     */
    public MovableEntity ent(){
        return ent;
    }

    /**
     * @return the list of players this Intel object is tracking.
     */
    public ArrayList<Player> getPlayers() {
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
    public void resetPlayers(ArrayList<Player> players) {
        this.players = players;
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
     * @return the health-value of the entity during the last tick.
     */
    public int healthLastTick() {
        return healthLastTick;
    }

    /**
     * Stores the entity's current health-value.
     * @param health - The health-value to be stored.
     */
    public void rememberHealth(int health) {
        this.healthLastTick = health;
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

    public VisibilityPolygon updateSight(){
        sight.visibilityFrom(ent.getPos());
        return sight;
    }

    public VisibilityPolygon getSight() {
        return sight;
    }

}
