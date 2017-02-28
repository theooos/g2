package objects;

import server.game.Vector2;

/**
 * Created by peran on 2/28/17.
 * Used to send player coords to the server
 */
public class MoveObject implements Sendable {
    private Vector2 pos;
    private Vector2 dir;
    private int ID;

    public MoveObject(Vector2 pos, Vector2 dir, int id) {
        this.dir = dir;
        this.ID = id;
        this.pos = pos;
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getDir() {
        return dir;
    }

    public int getID() {
        return ID;
    }
}

