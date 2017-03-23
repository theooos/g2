package objects;

/**
 * Created by peran on 2/26/17.
 * Used for switching phase
 */
public class PhaseObject implements Sendable {
    private int playerID;

    /**
     * An object for switching phase
     * @param playerID the player's phase to switch
     */
    public PhaseObject(int playerID) {
        this.playerID = playerID;
    }

    public int getID() {
        return playerID;
    }
}
