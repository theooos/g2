package objects;

import server.game.Player;
import server.game.Orb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by theo on 14/02/2017.
 */
public class InitGame implements Sendable{
    private HashMap<Integer,Orb> orbs;
    private HashMap<Integer,Player> players;
    private int mapID;

    public InitGame(HashMap<Integer,Orb> orbs, HashMap<Integer,Player> players, int mapID){
        this.orbs = orbs;
        this.players = players;
        this.mapID = mapID;
    }

    public InitGame(ArrayList<Orb> orbs, List<Player> players, int mapID) {
        this.mapID = mapID;

        this.orbs = new HashMap<>();
        for(Orb orb : orbs){
            this.orbs.put(orb.getID(), orb);
        }

        this.players = new HashMap<>();
        for(Player pla : players){
            this.players.put(pla.getID(), pla);
        }
    }

    public HashMap<Integer,Player> getPlayers() {
        return players;
    }

    public HashMap<Integer,Orb> getOrb() {
        return orbs;
    }

    public int getMapID() {
        return mapID;
    }
}
