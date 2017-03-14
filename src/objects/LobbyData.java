package objects;

import networking.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains data about a lobby that the client is in.
 */
public class LobbyData implements Sendable {

    private List<Integer> players;
    private int mapID;

    public LobbyData(ArrayList<Integer> players, int mapID) {
        this.players = players;
        this.mapID = mapID;
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public int getMapID() {
        return mapID;
    }
}
