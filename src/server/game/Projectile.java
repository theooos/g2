package server.game;

/**
 * Created by peran on 31/01/17.
 */
public class Projectile extends MovableEntity {

    protected int damage;
    protected int lifespan;
    protected int radius;

    public Projectile(int damage, int lifespan, int radius, Vector2 pos, Vector2 dir, float speed, int phase) {
        this.damage = damage;
        this.lifespan = lifespan;
        this.damagable = false;
        this.radius = radius;
        this.pos = pos;
        this.dir = dir;
        this.speed = speed;
        this.phase = phase;
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

    public int getRadius() {
        return radius;
    }
}
