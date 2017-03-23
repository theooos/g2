package objects;

import client.audio.AudioManager;
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
    private Scoreboard scoreboard;
    private int mapID;
    private LobbyData lobbyData;
    private Player me;

    /**
     * Sets up all the data for a game
     * @param initGame the start of the game
     */
    public GameData(InitGame initGame) {
        this.players = initGame.getPlayers();
        this.orbs = initGame.getOrbs();
        this.projectiles = new ConcurrentHashMap<>();
        this.mapID = initGame.getMapID();
        this.scoreboard = initGame.getScoreboard();
        this.powerUps = initGame.getPowerUps();
        this.lobbyData = initGame.getLobbyData();
    }

    /**
     * @return the players of the game
     */
    public ConcurrentHashMap<Integer, Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerID) {
        return players.get(playerID);
    }

    public ConcurrentHashMap<Integer, Projectile> getProjectiles() {
        return projectiles;
    }

    public void updateProjectile(Projectile p) {
        if (p.isAlive()) {
            if (p.getPlayer().equals(me) && projectiles.get(p.getID()) == null && p.getDamage() > 0) {
                AudioManager.playShooting(me);
            }
            projectiles.put(p.getID(), p);
        } else {
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
     *
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

    /**
     * Ignores certain things about the player received
     * @param p the player to extract data from
     */
    public void updateMe(Player p) {
        me = players.get(p.getID());
        me.setPhase(p.getPhase());
        me.setHealth(p.getHealth());
        me.setWeaponOut(p.isWeaponOneOut());
        me.setWeaponOutHeat(p.getWeaponOutHeat());
        me.setRadius(p.getRadius());
        me.setPhasePercentage(p.getPhase());
        players.put(p.getID(), me);
    }

    public void updateScoreboard(Scoreboard sb) {
        this.scoreboard = sb;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public HashMap<Integer, PowerUp> getPowerUps() {
        return powerUps;
    }

    public void updatePowerUp(PowerUp powerUp) {
        powerUps.put(powerUp.getID(), powerUp);
    }

    public LobbyData getLobbyData() {
        return lobbyData;
    }

    /**
     * @return Makes a human readable string
     */
    public java.lang.String toString() {
        java.lang.String string = "~ PLAYERS ~\n";
        for (Player player : players.values()) {
            string += player + "\n";
        }
        string += "~ ORBS ~\n";
        for (Orb orb : orbs.values()) {
            string += orb + "\n";
        }
        string += "~ PROJECTILES ~\n";
        for (Projectile projectile : projectiles.values()) {
            string += projectile + "\n";
        }
        return string;
    }
}
