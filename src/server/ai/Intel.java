package server.ai;

import server.game.Map;
import server.game.MovableEntity;
import server.game.Player;
import server.game.Vector2;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;

/**
 * A packet of the necessary data that AI units need to make decisions.
 * Created by rhys on 2/15/17.
 */
public class Intel {

    private MovableEntity ent;
    private ArrayList<Player> players;
    private Map map;
    private int healthLastTick;
    private boolean hurt;
    private boolean playerNearby;
    private boolean targetAcquired;
    private Vector2 targetLocation;
    private Player targetPlayer;
    private ArrayList<Vector2> path;


    /**
     * Constructs an intel object based on the given Players and Map.
     * @param players - The list of players.
     * @param map - The map currently in play.
     */
    public Intel(ArrayList<Player> players, Map map) {
        this.players = players;
        this.map = map;
        this.hurt = false;
        this.playerNearby = false;
        this.targetLocation = null;
        this.targetPlayer = null;
        path = new ArrayList<>();
    }

    public void assignEntity(MovableEntity ent){
        this.ent = ent;
        this.healthLastTick = ent.getHealth();
    }

    /**
     * @return the list of players this Intel object is tracking.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
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
     * @return the map this Intel is using.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Re-sets the map that this Intel is to use.
     * @param map - The desired map for refresh.
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /**
     * @return true if there is a player in the entity's knowledge region.
     */
    public boolean isPlayerNearby() {
        return playerNearby;
    }

    /**
     * Updates the entity's Intel to tell whether or not a player is nearby.
     * @param playerNearby
     */
    public void setPlayerNearby(boolean playerNearby) {
        this.playerNearby = playerNearby;
    }

    public int healthLastTick() {
        return healthLastTick;
    }

    public void rememberHealth(int health) {
        this.healthLastTick = health;
    }

    public boolean isInPain() {
        return hurt;
    }

    public void setHurt(boolean hurt) {
        this.hurt = hurt;
    }

    public Vector2 getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Vector2 targetLocation) {
        this.targetLocation = targetLocation;
    }

    /**
     * @return the position on the path currently moving towards.
     */
    public Vector2 checkpoint(){
        return path.get(0);
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
        path.clear();
        path = newPath;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    public MovableEntity ent(){
        return ent;
    }

    public void setTargetAcquired(boolean acquired) {
        this.targetAcquired = acquired;
    }

    public boolean isTargetAcquired(){
        return targetAcquired;
    }
}
