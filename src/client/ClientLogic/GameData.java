package client.ClientLogic;

import server.game.Orb;
import server.game.Player;
import server.game.Projectile;

import java.util.HashMap;

/**
 * Created by Patrick on 2/14/2017.
 * a gameData object with the status of the game.
 */

public class GameData {

    private HashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private HashMap<Integer, Projectile> projectiles;
    private int mapID;

    public GameData(HashMap players, HashMap orbs, HashMap projectiles, int id) {
        this.players = players;
        this.orbs = orbs;
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

    public void updateProjectile(Projectile p) {
        if (p.isAlive()) {
            projectiles.put(p.getID(), p);
        }
        else {
            projectiles.remove(p.getID());
        }
    }

    /**
     * @return the orbs
     */
    public HashMap<Integer, Orb> getOrbs() {
        return orbs;
    }

    /**
     * @return the mapID
     */
    public int getMapID() {
        return mapID;
    }

    /**
     * update the hashmap of the orbs.
     * @param o the orb to be changed.
     */
    public void updateOrb(Orb o) {
        orbs.put(o.getID(), o);
    }

    /**
     * update the hashmap of the players
     */
    public void updatePlayer(Player p) {
        players.put(p.getID(), p);
    }

    public void updateMe(Player p) {
        Player me = players.get(p.getID());
        me.setPhase(p.getPhase());
        me.setHealth(p.getHealth());
        players.put(p.getID(), me);
    }

}
