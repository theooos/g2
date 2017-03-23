package objects;

/**
 * Contains data about a lobby that the client is in.
 */
public class LobbyData implements Sendable {

    private InitPlayer[] players;
    private int mapID;

    /**
     * All the information in a lobby
     * @param players the players in the lobby
     * @param mapID the id of the map
     */
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
