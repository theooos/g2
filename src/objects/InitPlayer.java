package objects;

import java.util.Objects;

/**
 * Created by peran on 3/20/17.
 * Used to initialise players in the lobby
 */
public class InitPlayer implements Sendable {
    private int ID;
    private String name;
    private boolean isAI;
    private int team;

    /**
     * Initialises a player
     * @param i the ID of the player
     * @param name the name of the player
     * @param isAI whether the player is linked to a connection
     * @param team which team the player is on
     */
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

    public boolean equals(InitPlayer p) {
        return p.getID() == ID && p.getName().toString().equals(name.toString()) && p.isAI() == isAI() && p.getTeam() == team;
    }
}
