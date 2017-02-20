package objects;

import server.game.Orb;
import server.game.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by theo on 14/02/2017.
 */
public class InitGame implements Sendable{
    private HashMap<Integer,Orb> zombies;
    private HashMap<Integer,Player> players;
    private int mapID;

    public InitGame(HashMap<Integer,Orb> zombies, HashMap<Integer,Player> players, int mapID){
        this.zombies = zombies;
        this.players = players;
        this.mapID = mapID;
    }

    public InitGame(ArrayList<Orb> orbs, ArrayList<Player> players, int mapID) {
        this.mapID = mapID;

        this.zombies = new HashMap<>();
        for(Orb zom : orbs){
            this.zombies.put(zom.getID(), zom);
        }

        this.players = new HashMap<>();
        for(Player pla : players){
            this.players.put(pla.getID(), pla);
        }
    }

    public HashMap<Integer,Player> getPlayers() {
        return players;
    }

    public HashMap<Integer,Orb> getZombies() {
        return zombies;
    }

    public int getMapID() {
        return mapID;
    }
}
