package client.ClientLogic;

import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Patrick on 2/14/2017.
 * a gameData object with the status of the game.
 */

public class GameData {

    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private ConcurrentHashMap<Integer, Projectile> projectiles;
    private HashMap<Integer, PowerUp> powerUps;
    private Scoreboard sb;
    private int mapID;

    GameData(ConcurrentHashMap<Integer, Player> players, HashMap<Integer, Orb> orbs, ConcurrentHashMap<Integer, Projectile> projectiles, int id, Scoreboard sb, HashMap<Integer, PowerUp> powerUps) {
        this.players = players;
        this.orbs = orbs;
        this.projectiles = projectiles;
        this.mapID = id;
        this.sb = sb;
        this.powerUps = powerUps;
    }

    /**
     * @return the players of the game
     */
    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerID){
        return players.get(playerID);
    }

    public ConcurrentHashMap<Integer, Projectile> getProjectiles() {return  projectiles;}

    void updateProjectile(Projectile p) {
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
    void updateOrb(Orb o) {
        orbs.put(o.getID(), o);
    }

    /**
     * update the hashmap of the players
     */
    public void updatePlayer(Player p) {
        players.put(p.getID(), p);
    }

    void updateMe(Player p) {
        Player me = players.get(p.getID());
        me.setPhase(p.getPhase());
        me.setHealth(p.getHealth());
        me.setWeaponOut(p.isWeaponOneOut());
        me.setWeaponOutHeat(p.getWeaponOutHeat());
        players.put(p.getID(), me);
    }

    void updateScoreboard(Scoreboard sb) {
        this.sb = sb;
    }

    public Scoreboard getSb() {
        return sb;
    }

    public HashMap<Integer, PowerUp> getPowerUps() {
        return powerUps;
    }

    public void updatePowerUp(PowerUp powerUp) {
        powerUps.put(powerUp.getID(), powerUp);
    }

}
