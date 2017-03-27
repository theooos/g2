package server.game;

import objects.Sendable;

/**
 * Created by peran on 3/9/17.
 * A class for certain modifiers on the battle field
 */
public class PowerUp extends MovableEntity implements Sendable {
    public Type type;
    private boolean changed;

    /**
     * Creates a new power up
     * @param pos the position
     * @param t the type of the power up
     * @param id it's unique ID
     * @param phase the phase the power up occupies
     */
    public PowerUp(Vector2 pos, Type t, int id, int phase) {
        this.pos = pos;
        this.type = t;
        this.radius = 10;
        this.ID = id;
        changed = false;
        this.phase = phase;
        respawnTime = 300;
        maxHealth= 1;
    }

    /**
     * Checks to see if the power up has been touched to reduce network load
     * @return if changed or not
     */
    boolean isChanged() {
        return changed;
    }

    /**
     * Sets if the power up has been changed
     * @param changed whether it's been changed
     */
    void setChanged(boolean changed) {
        this.changed = changed;
    }

    public Type getType() {
        return type;
    }

    /**
     * the types of the power up.
     * Health restores player health
     * Heat refreshes the player's heat bar.
     */
    public enum Type {
        health, heat
    }
}
