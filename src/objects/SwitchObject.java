package objects;

/**
 * Created by peran on 4/26/17.  Used to switch weapons from client to server
 */
public class SwitchObject implements Sendable {
    private int playerID;
    private boolean weaponOne;

    /**
     * And object for weapon switching
     * @param id the player id
     * @param weaponOne whether weapon one is out
     */
    public SwitchObject(int id, boolean weaponOne) {
        this.playerID = id;
        this.weaponOne = weaponOne;
    }

    public boolean takeWeaponOneOut() {
        return weaponOne;
    }

    public int getID() {
        return playerID;
    }
}
