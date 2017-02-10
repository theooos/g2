package server.game;

import objects.Sendable;

import java.util.ArrayList;

/**
 * Created by peran on 01/02/17.
 */
public class Weapon implements Sendable{
    protected int accuracy;
    protected int recoil;
    protected int recoilRecovery;
    protected int magSize;
    protected int heatPerShot;
    protected int currentHeat;
    protected int coolDownRate;
    protected Projectile shotType;
    protected int numProjectiles;

    /**
     * skeleton class to represent future weapons
     * will probably use inheritence
     */
    public Weapon() {
        magSize = 100;
        currentHeat = 0;
        shotType = new Projectile(100,100,20,new Vector2(0,0), new Vector2(0,0), 100, 0, 0, 0);
        numProjectiles = 1;
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

    ArrayList<Projectile> getShots(Player player) {
        ArrayList<Projectile> ps = new ArrayList<>();
        for (int i = 0; i < numProjectiles; i++) {
            ps.add(shotType);
            ps.get(i).setDir(player.getDir());
            ps.get(i).setPos(player.getPos());
            ps.get(i).setPlayerID(player.getID());
        }
        return ps;
    }

    protected Vector2 getDeviation(Vector2 v) {
        
        return v;
    }
}
