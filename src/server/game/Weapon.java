package server.game;

import objects.Sendable;

/**
 * Created by peran on 01/02/17.
 */
public class Weapon implements Sendable{
    protected float accuracy;
    protected float recoil;
    protected float recoilRecovery;
    protected int magSize;
    protected int heatPerShot;
    protected int currentHeat;
    protected int coolDownRate;
    protected Projectile shotType;

    /**
     * skeleton class to represent future weapons
     * will probably use inheritence
     */
    public Weapon() {
        magSize = 100;
        currentHeat = 0;
    }

    public void live() {
        if (currentHeat > coolDownRate) {
            currentHeat -= coolDownRate;
        } else if (currentHeat > 0) {
            currentHeat = 0;
        }
    }

    public void fire(Vector2 pos, Vector2 dir) {
        if (magSize-currentHeat < heatPerShot) return;
        currentHeat += heatPerShot;
        //create projectiles or sommit
    }


}
