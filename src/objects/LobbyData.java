package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains data about a lobby that the client is in.
 */
public class LobbyData implements Sendable {

    private InitPlayer[] players;
    private int mapID;

    public LobbyData(InitPlayer[] players, int mapID) {
        this.players = players;
        this.mapID = mapID;
    }

    public InitPlayer[] getPlayers() {
        return players;
    }

    public int getMapID() {
        return mapID;
    }
}
