package objects;

/**
 * Created by peran on 2/26/17.
 * Used to notify the server of gun firing
 */
public class FireObject implements Sendable {
    private int playerID;
    private boolean startFire;

    /**
     * Used to tell a server whether to stop or start firing
     * @param id the player id which is firing
     * @param start whether to stop or start firing
     */
    public FireObject(int id, boolean start) {
        this.playerID = id;
        this.startFire = start;
    }

    public boolean isStartFire() {
        return startFire;
    }

    public int getPlayerID() {
        return playerID;
    }
}
