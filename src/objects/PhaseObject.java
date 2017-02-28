package objects;

/**
 * Created by peran on 2/26/17.
 */
public class PhaseObject implements Sendable {
    private int playerID;

    public PhaseObject(int playerID) {
        this.playerID = playerID;
    }

    public int getID() {
        return playerID;
    }
}
