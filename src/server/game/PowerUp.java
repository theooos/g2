package server.game;

import objects.Sendable;

/**
 * Created by peran on 3/9/17.
 * A class for game changing
 */
public class PowerUp extends MovableEntity implements Sendable {
    public Type type;
    private boolean changed;

    PowerUp(Vector2 pos, Type t, int id, int phase) {
        this.pos = pos;
        this.type = t;
        this.radius = 10;
        this.ID = id;
        changed = false;
        this.phase = phase;
        respawnTime = 300;
        maxHealth= 1;
    }

    boolean isChanged() {
        return changed;
    }

    void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        health, heat
    }
}
