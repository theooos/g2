package objects;

/**
 * Created by peran on 3/20/17.
 * Used to initialise players in the lobby
 */
public class InitPlayer implements Sendable {
    private int ID;
    private String name;
    private boolean isAI;
    private int team;

    public InitPlayer(int i, String name, boolean isAI, int team) {
        ID = i;
        this.name = name;
        this.isAI = isAI;
        this.team = team;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public boolean isAI() {
        return isAI;
    }

    public int getTeam() {
        return team;
    }
}
