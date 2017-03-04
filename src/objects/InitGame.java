package objects;

import server.game.Player;
import server.game.Orb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by theo on 14/02/2017.
 */
public class InitGame implements Sendable{
    private HashMap<Integer,Orb> orbs;
    private ConcurrentHashMap<Integer,Player> players;
    private int mapID;

    public InitGame(HashMap<Integer,Orb> orbs, ConcurrentHashMap<Integer,Player> players, int mapID){
        this.orbs = orbs;
        this.players = players;
        this.mapID = mapID;
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
}
