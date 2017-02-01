package server.game;

import java.awt.*;

/**
 * Created by peran on 27/01/17.
 */
public class Entity {
    protected boolean damagable;
    protected Vector2 pos;
    protected int health;
    protected int phase;

    /**
     * A class intended for inheritence, should not be created
     */
    public Entity() {}

    public void updatePos(float x, float y) {
        updatePos(new Vector2(x, y));
    }

    public void updatePos(Vector2 pos) {
        this.pos = pos;
    }

    public void setDamagable(boolean d) {
        damagable = d;
    }

    public boolean getDamagable() {
        return damagable;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        if (health < 1) {
            return false;
        }

        return true;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
