package server.ai;

import server.game.Map;
import server.game.Player;

import java.util.ArrayList;

/**
 * A packet of the necessary data that AI units need to make decisions.
 * Created by rhys on 2/15/17.
 */
public class Intel {

    private ArrayList<Player> players;
    private Map map;

    /**
     * Constructs an intel object based on the given Players and Map.
     * @param players - The list of players.
     * @param map - The map currently in play.
     */
    public Intel(ArrayList<Player> players, Map map) {
        this.players = players;
        this.map = map;
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
}
