package server.game;

/**
 * Created by peran on 27/01/17.
 * A father class for all entities in the game
 */
public abstract class Entity implements objects.Sendable {
    boolean damageable;
    protected Vector2 pos;
    int maxHealth;
    protected int health;
    protected int phase;
    boolean visible;
    protected int ID;

    boolean getDamageable() {
        return damageable;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return health >= 1;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    boolean getVisible() {
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

    public String toString(){
        return "ID: "+getID()+" POS: "+getPos()+" PHASE: "+getPhase();
    }
}
