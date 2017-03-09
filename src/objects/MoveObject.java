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
    private int moveCounter;

    public MoveObject(Vector2 pos, Vector2 dir, int id, int moveCounter) {
        this.dir = dir;
        this.ID = id;
        this.pos = pos;
        this.moveCounter = moveCounter;
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

    public java.lang.String toString(){
        return "Pos: " + pos + " Dir: " + dir + " ID: " + ID;
    }

    public int getMoveCounter() {
        return moveCounter;
    }
}

