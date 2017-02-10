package server.game;

/**
 * Created by rhys on 2/10/17.
 */
public class SpawnPosition {

    private Vector2 pos;
    private int team;

    public SpawnPosition(Vector2 pos, int team){
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
