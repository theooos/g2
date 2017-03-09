package objects;

import server.game.Player;
import server.game.Orb;
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
    private int mapID;
    private Scoreboard sb;

    public InitGame(HashMap<Integer,Orb> orbs, ConcurrentHashMap<Integer,Player> players, int mapID, Scoreboard sb){
        this.orbs = orbs;
        this.players = players;
        this.mapID = mapID;
        this.sb = sb;
    }

    public ConcurrentHashMap<Integer,Player> getPlayers() {
        return players;
    }

    public HashMap<Integer,Orb> getOrb() {
        return orbs;
    }

    public int getMapID() {
        return mapID;
    }

    public Scoreboard getSb() {
        return sb;
    }
}
