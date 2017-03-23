package server.game;

/**
 * Created by peran on 27/01/17.
 * All major entities inherit from this such as players, orbs, and projectiles
 */
public abstract class MovableEntity extends Entity {
    protected float speed;
    protected Vector2 dir;
    protected float radius;
    int team;
    int respawnTime;
    int timeTillRespawn;

    /**
     * moves the player by it's speed
     */
    protected void move() {
        this.pos = pos.add(dir.mult(speed));
    }

    public float getSpeed() {
        return speed;
    }

    /**
     * Allows the Orb to change its speed depending on how it's feeling.
     * @param newSpeed - the Orb's new speed.
     */
    public void setSpeed(float newSpeed){
        this.speed = newSpeed;
    }

    public Vector2 getDir() {
        return  dir;
    }

    public void setDir(Vector2 dir) {
        this.dir = dir;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return (int) radius;
    }

    public int getTeam() {
        return team;
    }

    /**
     * Allows for the entity to respawn
     */
    void live() {
        if (!isAlive()) timeTillRespawn--;
    }

    boolean canRespawn() {
        return (timeTillRespawn <= 0);
    }

    /**
     * sets the timer to max spawn time
     */
    void resetTimeTillRespawn() {
        timeTillRespawn = respawnTime;
    }
}
