package server.game;

/**
 * Created by peran on 27/01/17.
 */
public class Entity implements objects.Sendable {
    protected boolean damageable;
    protected Vector2 pos;
    protected int maxHealth;
    protected int health;
    protected int phase;
    protected boolean visible;
    protected int ID;


    /**
     * A class intended for inheritance, should not be created
     */
    public Entity(int id) {
        ID = id;
    }

    public void updatePos(float x, float y) {
        updatePos(new Vector2(x, y));
    }

    public void updatePos(Vector2 pos) {
        this.pos = pos;
    }

    public void setDamageable(boolean d) {
        damageable = d;
    }

    public boolean getDamageable() {
        return damageable;
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

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean getVisible() {
        return visible;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void damage(int d) {
        health -= d;
    }

    public int getID() {
        return ID;
    }

    public boolean equals(Object o) {
        try {
            return ((Entity) o).getID() == ID;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void setID(int i) {
        this.ID = i;
    }
}
