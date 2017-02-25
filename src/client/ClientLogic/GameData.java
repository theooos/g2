package client.ClientLogic;

import server.game.Player;
import server.game.Zombie;

import java.util.HashMap;

/**
 * Created by Patrick on 2/14/2017.
 * a gameData object with the status of the game.
 */

public class GameData {

    private HashMap<Integer, Player> players;
    private HashMap<Integer, Zombie> zombies;
    private int mapID;

    public GameData(HashMap players, HashMap zombies, int id) {
        this.players = players;
        this.zombies = zombies;
        this.mapID = id;
    }

    /**
     * @return the players of the game
     */
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerID){
        return players.get(playerID);
    }

    /**
     * @return the zombies
     */
    public HashMap<Integer, Zombie> getZombies() {
        return zombies;
    }

    /**
     * @return the mapID
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * update the hashmap of the zombies
     *
     * @param zombieId the id
     * @param z        the zombie to be changed.
     */
    public void updateZombies(int zombieId, Zombie z) {
        zombies.put(zombieId, z);
    }

    /**
     * update the hashmap of the players
     */
    public void updatePlayer(Player p) {
        players.put(p.getID(), p);
    }


}
