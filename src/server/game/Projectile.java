package server.game;

/**
 * Created by peran on 31/01/17.
 */
public class Projectile extends MovableEntity {

    protected int damage;
    protected int lifespan;
    protected int playerID;

    /**
     * A boring default projectile
     * @param damage the damage at birth
     * @param lifespan the lifespan before the prj dies
     * @param radius the radius of the prj at the start
     * @param pos the starting pos of the prj
     * @param dir the starting direction of the prj
     * @param speed the speed of the prj
     * @param phase the phase the prj is in
     */
    public Projectile(int damage, int lifespan, int radius, Vector2 pos, Vector2 dir, float speed, int phase, int playerID, int id) {
        this.damage = damage;
        this.lifespan = lifespan;
        this.damageable = false;
        this.visible = true;
        this.radius = radius;
        this.pos = pos;
        this.dir = dir;
        this.speed = speed;
        this.phase = phase;
        ID = id;
    }

    public void live() {
        move();
        tickLife();
    }

    public int getDamage() {
        return damage;
    }

    protected void tickLife() {
        lifespan--;
        if (lifespan < 1) {
            setHealth(0);
        }
    }

    /**
     * sets the lifespan of a proj to 0
     */
    void kill() {
        lifespan = 0;
        health = 0;
    }

    int getPlayerID() {
        return playerID;
    }

    void setPlayerID(int id) {
        this.playerID = id;
    }
}
