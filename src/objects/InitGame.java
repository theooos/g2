package objects;

import server.game.Player;
import server.game.Orb;
import server.game.PowerUp;
import server.game.Scoreboard;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by theo on 14/02/2017.
 * Used as an object to set up the start of the game
 */
public class InitGame implements Sendable{
    private HashMap<Integer,Orb> orbs;
    private ConcurrentHashMap<Integer,Player> players;
    private HashMap<Integer, PowerUp> powerUps;
    private int mapID;
    private Scoreboard sb;
    private LobbyData ld;


    /**
     * Starts a game
     * @param orbs all the orbs in the game
     * @param players all the players in the game (inc AI)
     * @param mapID the map id to be playing on
     * @param sb the scoreboard
     * @param powerUps all the power ups in the game
     * @param ld the lobby data for drawing the scoreboard
     */
    public InitGame(HashMap<Integer, Orb> orbs, ConcurrentHashMap<Integer, Player> players, int mapID, Scoreboard sb, HashMap<Integer, PowerUp> powerUps, LobbyData ld){
        this.orbs = orbs;
        this.players = players;
        this.mapID = mapID;
        this.sb = sb;
        this.powerUps = powerUps;
        this.ld = ld;
    }

    public ConcurrentHashMap<Integer,Player> getPlayers() {
        return players;
    }

    public HashMap<Integer,Orb> getOrbs() {
        return orbs;
    }

    public int getMapID() {
        return mapID;
    }

    public Scoreboard getScoreboard() {
        return sb;
    }

    public HashMap<Integer, PowerUp> getPowerUps() {
        return powerUps;
    }

    public LobbyData getLobbyData() {
        return ld;
    }
}
