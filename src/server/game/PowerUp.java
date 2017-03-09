package server.game;

/**
 * Created by peran on 3/9/17.
 * A class for game changing
 */
public class PowerUp {
    private Vector2 pos;
    private int radius;
    public Type type;
    private int ID;
    private boolean changed;

    PowerUp(Vector2 pos, Type t, int id) {
        this.pos = pos;
        this.type = t;
        this.radius = 10;
        this.ID = id;
        changed = false;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public enum Type {
        health, heat
    }

    public Vector2 getPos() {
        return pos;
    }

    public int getRadius() {
        return radius;
    }

    public int getID() {
        return ID;
    }
}
