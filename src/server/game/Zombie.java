package server.game;

/**
 * Created by peran on 01/02/17.
 */
public class Zombie extends MovableEntity {

    private int team;

    /**
     * The basic AI controlled enemy
     * @param pos starting pos
     * @param dir starting dir
     * @param team the team the is on
     * @param phase starting phase
     */
    public Zombie(Vector2 pos, Vector2 dir, int team, int phase) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damageable = true;
        this.visible = true;
        maxHealth = 50;
        this.health = maxHealth;
        radius = 10;
    }

    public void live() {
        move();
        //any other methods the zombie may do once a tick
    }

    protected void move() {
        //ai basic movement for the zombie
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
