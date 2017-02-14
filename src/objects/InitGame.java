package objects;

import server.game.Player;
import server.game.Zombie;

import java.util.ArrayList;

/**
 * Created by theo on 14/02/2017.
 */
public class InitGame implements Sendable{
    private ArrayList<Zombie> zombies;
    private ArrayList<Player> players;
    private int mapID;

    public InitGame(ArrayList<Zombie> z, ArrayList<Player> p, int mapID) {
        this.mapID = mapID;
        this.players = p;
        this.zombies = z;
    }


    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }

    public int getMapID() {
        return mapID;
    }
}
