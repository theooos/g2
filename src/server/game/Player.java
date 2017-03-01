package server.game;

/**
 * Created by peran on 01/02/17.
 * The entity which players control.
 * AI players are a descendant of these
 */
public class Player extends MovableEntity {

    private Weapon w1;
    private Weapon w2;
    private boolean w1Out;
    private boolean firing;

    /**
     * The basic player class
     * @param pos starting pos
     * @param dir starting dir
     * @param team the team the player is in
     * @param phase starting phase
     */
    public Player(Vector2 pos, Vector2 dir, int team, int phase, Weapon w1, Weapon w2, int id) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damageable = true;
        this.visible = true;
        maxHealth = 100;
        this.health = maxHealth;
        this.w1 = w1;
        this.w2 = w2;
        this.speed = 5;
        radius = 20;
        w1Out = true;
        this.team = team;
        ID = id;
        firing = false;
    }

    public void live() {
        //any methods the player may do once a tick
        move();
        getActiveWeapon().live();
    }

    public void move() {
        super.move();
    }



    Weapon getActiveWeapon() {
        if (w1Out) return w1;
        else return w2;
    }

    void toggleWeapon() {
        w1Out = !w1Out;
    }

    void togglePhase() {
        if (phase == 1) phase = 0;
        else phase = 1;
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }
}
