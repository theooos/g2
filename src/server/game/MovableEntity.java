package server.game;

/**
 * Created by peran on 27/01/17.
 * All major entities inherit from this such as players, orbs, and projectiles
 */
public abstract class MovableEntity extends Entity {
    protected float speed;
    protected Vector2 dir;
    float radius;
    int team;
    int respawnTime;
    private int timeTillRespawn;

    protected void move() {
        this.pos = pos.add(dir.mult(speed));
    }

    public Vector2 hypoMove() {
        return pos.add(dir.mult(speed));
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

    public int getRadius() {
        return (int) radius;
    }

    public int getTeam() {
        return team;
    }

    void live() {
        if (!isAlive()) timeTillRespawn--;
    }

    boolean canRespawn() {
        return (timeTillRespawn <= 0);
    }

    void resetTimeTillRespawn() {
        timeTillRespawn = respawnTime;
    }
}
