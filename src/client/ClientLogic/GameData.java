package client.ClientLogic;

import server.game.Player;
import server.game.Projectile;
import server.game.Zombie;

import java.util.HashMap;

/**
 * Created by Patrick on 2/14/2017.
 * a gameData object with the status of the game.
 */

public class GameData {

    private HashMap<Integer, Player> players;
    private HashMap<Integer, Zombie> zombies;
    private HashMap<Integer, Projectile> projectiles;
    private int mapID;

    public GameData(HashMap players, HashMap zombies, HashMap projectiles, int id) {
        this.players = players;
        this.zombies = zombies;
        this.projectiles = projectiles;
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

    public HashMap<Integer, Projectile> getProjectiles() {return  projectiles;}

    public void updateProjectile(Projectile p) {projectiles.put(p.getID(), p);}

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
     * @param z the zombie to be changed.
     */
    public void updateZombie(Zombie z) {
        zombies.put(z.getID(), z);
    }

    /**
     * update the hashmap of the players
     */
    public void updatePlayer(Player p) {
        players.put(p.getID(), p);
    }

}
