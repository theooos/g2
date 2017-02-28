package server.game;

/**
 * Created by peran on 01/02/17.
 */
public class Zombie extends MovableEntity {

    /**
     * The basic AI controlled enemy
     * @param pos starting pos
     * @param dir starting dir
     * @param phase starting phase
     */
    public Zombie(Vector2 pos, Vector2 dir, int phase, int id) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damageable = true;
        this.visible = true;
        maxHealth = 50;
        this.health = maxHealth;
        this.speed = 2;
        radius = 10;
        ID = id;
    }

    public void live() {
        move();
        //any other methods the zombie may do once a tick
    }

    protected void move() {
        //ai basic movement for the zombie
        pos = pos.add(dir.mult(speed));
    }
}
