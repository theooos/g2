package server.game;

import static server.game.ServerConfig.PHASE_FADE_TIME;

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
    private double weaponOutHeat;
    private int moveCounter;
    private float phasePercentage;

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
        ID = id;
        firing = false;
        weaponOutHeat = 0;
        moveCounter = 0;
        respawnTime = 240;
        timeTillRespawn = respawnTime;
        phasePercentage = phase;
    }

    public Player(Player player) {
        this.pos = player.getPos();
        this.dir = player.getDir();
        this.team = player.getTeam();
        this.phase = player.getPhase();
        this.damageable = player.getDamageable();
        this.visible = player.getVisible();
        maxHealth = 100;
        this.health = player.getHealth();
        this.w1 = player.w1;
        this.w2 = player.w2;
        this.speed = player.speed;
        this.radius = player.getRadius();
        this.w1Out = player.w1Out;
        this.ID = player.getID();
        this.firing = player.isFiring();
        this.weaponOutHeat = player.getWeaponOutHeat();
        this.moveCounter = player.getMoveCount();
        respawnTime = 240;
        timeTillRespawn = respawnTime;
        phasePercentage = phase;
    }

    public void live() {
        super.live();
        //any methods the player may do once a tick
        getActiveWeapon().live();
        weaponOutHeat = getActiveWeapon().getHeat();
        if (phasePercentage != phase) {
            if (phasePercentage > phase) {
                phasePercentage -= 1 / PHASE_FADE_TIME;
            } else {
                phasePercentage += 1 / PHASE_FADE_TIME;
            }

            if (Math.abs(phasePercentage-phase) < 0.05) {
                phasePercentage = phase;
            } else if (phasePercentage > 1) {
                phasePercentage = 1;
            } else if (phasePercentage < 0) {
                phasePercentage = 0;
            }
        }
    }

    public void move() {
        super.move();
    }

    public Weapon getActiveWeapon() {
        if (w1Out) return w1;
        else return w2;
    }

    public boolean isWeaponOneOut() {
        return w1Out;
    }

    public void setWeaponOut(boolean w1out) {
        this.w1Out = w1out;
    }

    void togglePhase() {
        if (phase == 1) phase = 0;
        else phase = 1;
    }

    boolean isFiring() {
        return firing;
    }

    void setFiring(boolean firing) {
        this.firing = firing;
    }

    public double getWeaponOutHeat() {
        return weaponOutHeat;
    }

    public void setWeaponOutHeat(double weaponOutHeat) {
        this.weaponOutHeat = weaponOutHeat;
    }

    public int getMoveCount() {
        return moveCounter;
    }

    void incMove() {
        moveCounter++;
    }

    public void setMoveCount(int moveCount) {
        this.moveCounter = moveCount;
    }

    public float getPhasePercentage() {
        return phasePercentage;
    }

    public void setPhasePercentage(float phasePercentage) {
        this.phasePercentage = phasePercentage;
    }
}

