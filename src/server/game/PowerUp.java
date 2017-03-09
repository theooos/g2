package server.game;

import objects.Sendable;

/**
 * Created by peran on 3/9/17.
 * A class for game changing
 */
public class PowerUp implements Sendable {
    private Vector2 pos;
    private int radius;
    public Type type;
    private int ID;
    private boolean changed;
    private int phase;

    PowerUp(Vector2 pos, Type t, int id, int phase) {
        this.pos = pos;
        this.type = t;
        this.radius = 10;
        this.ID = id;
        changed = false;
        this.phase = phase;
    }

    boolean isChanged() {
        return changed;
    }

    void setChanged(boolean changed) {
        this.changed = changed;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        health, heat
    }

    void setPos(Vector2 pos) {
        this.pos = pos;
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
