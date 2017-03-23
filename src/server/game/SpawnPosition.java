package server.game;

/**
 * Created by rhys on 2/10/17.
 * A class to enable spawn points on maps
 */
public class SpawnPosition {

    private Vector2 pos;
    private int team;

    SpawnPosition(Vector2 pos, int team){
        this.pos = pos;
        this.team = team;
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getTeam() {
        return team;
    }
}
