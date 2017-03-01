package objects;

/**
 * Created by peran on 2/26/17.
 */
public class FireObject implements Sendable {
    private int playerID;
    private boolean startFire;

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
