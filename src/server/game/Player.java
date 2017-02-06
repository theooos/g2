package server.game;

/**
 * Created by peran on 01/02/17.
 */
public class Player extends MovableEntity {

    protected int team;
    protected Weapon w1;
    protected Weapon w2;
    protected boolean w1Out;

    /**
     * The basic player class
     * @param pos starting pos
     * @param dir starting dir
     * @param team the team the player is in
     * @param phase starting phase
     */
    public Player(Vector2 pos, Vector2 dir, int team, int phase, Weapon w1, Weapon w2) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damagable = true;
        this.visible = true;
        this.health = 100;
        this.w1 = w1;
        this.w2 = w2;
        w1Out = true;
    }

    public void live() {
        move();
        //any other methods the player may do once a tick
    }

    protected void move() {
        //movement based on player input
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}