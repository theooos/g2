package server.game;

import server.ai.OrbBrain;
import server.ai.Intel;

/**
 * Created by peran on 01/02/17.
 */
public class Orb extends MovableEntity implements Runnable {

    private Intel intel;
    private OrbBrain myBrain;
    Thread thread;
    private boolean firstRun;

    /**
     * The basic AI controlled enemy
     * @param pos starting pos
     * @param dir starting dir
     * @param team the team the is on
     * @param phase starting phase
     */
    public Orb(Vector2 pos, Vector2 dir, int team, int phase, int id, Intel intel) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damageable = true;
        this.visible = true;
        maxHealth = 50;
        this.health = maxHealth;
        this.speed = 2;
        this.team = team;
        radius = 10;
        ID = id;
        this.intel = intel;
        this.myBrain = new OrbBrain(intel);
        intel.assignEntity(this);

        thread = new Thread(this, "Orb01");
        thread.start();
    }

    /**
     * Makes this Orb behave in an appropriate manner when triggered by the Game Loop.
     */
    public void live() {
        myBrain.doSomething();
    }

    @Override
    public void run() {
        while(true) {
            live();
            try {
                thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
