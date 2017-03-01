package objects;

import server.game.Player;
import server.game.Zombie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by theo on 14/02/2017.
 */
public class InitGame implements Sendable{
    private HashMap<Integer,Zombie> zombies;
    private HashMap<Integer,Player> players;
    private int mapID;

    public InitGame(HashMap<Integer,Zombie> zombies, HashMap<Integer,Player> players, int mapID){
        this.zombies = zombies;
        this.players = players;
        this.mapID = mapID;
    }

    public InitGame(ArrayList<Zombie> zombies, List<Player> players, int mapID) {
        this.mapID = mapID;

        this.zombies = new HashMap<>();
        for(Zombie zom : zombies){
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

    public HashMap<Integer,Zombie> getZombies() {
        return zombies;
    }

    public int getMapID() {
        return mapID;
    }
}
