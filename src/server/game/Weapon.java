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
        shotType = new DistDropOffProjectile(100,100,20,new Vector2(0,0), new Vector2(0,0), 100, 0, 0, 0);
    }

    void live() {
        if (currentHeat > coolDownRate) {
            currentHeat -= coolDownRate;
        } else if (currentHeat > 0) {
            currentHeat = 0;
        }
    }

    boolean canFire() {
        if (magSize-currentHeat < heatPerShot) return false;
        currentHeat += heatPerShot;
        return true;
    }

    Projectile getShotType() {
        return shotType;
    }


}
