package server.game;

import objects.Sendable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by peran on 01/02/17.
 * A default weapon class with example stats
 */
class Weapon implements Sendable{
    int accuracy;
    int maxRecoil;
    double recoilRecovery;
    private double currentRecoil;
    private int magSize;
    int bloomPerShot;
    int heatPerShot;
    private int currentHeat;
    double coolDownRate;
    Projectile shotType;
    int numProjectiles;
    private Random rand;
    int refireTime;
    private int refireDelay;
    boolean fullyAuto;


     Weapon() {
        //with a cooldown rate of 1, it'll take 2s for a full reload
        magSize = 120;
        currentHeat = 0;
        shotType = new Projectile(100,100,20,new Vector2(0,0), new Vector2(0,0), 100, 0, 0, 0);
        numProjectiles = 1;
        //how much current recoil there is
        currentRecoil = 0;
        //the max inaccuracy from recoil alone
        maxRecoil = 50;
        //base degree of inaccuracy
        accuracy = 15;
        //degrees of inaccuracy added depending on weapon type
        //loads for snipers, less for fully auto guns
        bloomPerShot = 5;
        //how many degrees of accuracy is recovered per tick
        recoilRecovery = 1;
        //the speed the weapon cools down
        coolDownRate = 0.5;
        //the amount of heat each firing of the weapon takes up
        heatPerShot = 4;
        //min time after firing a shot till the gun can fire again
        refireTime = 13;
        refireDelay = 0;
        //whether the player can hold down the trigger
        fullyAuto = true;
        rand = new Random();
    }

    void live() {
        if (currentHeat > coolDownRate) {
            currentHeat -= coolDownRate;
        } else if (currentHeat > 0) {
            currentHeat = 0;
        }
        if (refireDelay > 0) {
            refireDelay--;
        }
        recoverRecoil();
    }

    private void recoverRecoil() {
        if (currentRecoil < 0) {
            currentRecoil = 0;
        }
        else if (currentRecoil != 0) {
            currentRecoil -= recoilRecovery;
        }
    }

    boolean canFire() {
        return (magSize - currentHeat >= heatPerShot) && refireDelay <= 0;
    }

    ArrayList<Projectile> getShots(Player player) {
        ArrayList<Projectile> ps = new ArrayList<>();
        for (int i = 0; i < numProjectiles; i++) {
            ps.add(shotType);
            ps.get(i).setDir(getDeviation(player.getDir()));
            ps.get(i).setPos(player.getPos());
            ps.get(i).setPlayerID(player.getID());
        }
        currentRecoil += bloomPerShot;
        if (currentRecoil > maxRecoil) currentRecoil = maxRecoil;
        currentHeat += heatPerShot;
        refireDelay = refireTime;
        return ps;
    }

    private Vector2 getDeviation(Vector2 v) {
        //x' = xcosf - ysinf
        //y' = ycosf - xsinf
        double ang = Math.toDegrees(Math.cos((double) v.getX()));
        ang += rand.nextInt(2*(int)(accuracy+currentRecoil))-accuracy+currentRecoil;
        float newX = (float)(v.getX()*Math.cos(ang) - v.getY()*Math.sin(ang));
        float newY = (float)(v.getY()*Math.cos(ang) - v.getX()*Math.sin(ang));

        return new Vector2(newX, newY);
    }

    boolean isFullyAuto() {
         return fullyAuto;
    }

  /*  public static void main(String[] args) {
        Vector2 v = new Vector2(1,0);
        Weapon w = new Weapon();
        Weapon w1 = new WeaponShotgun();
        Weapon w2 = new WeaponSniper();
        Weapon w3 = new WeaponSMG();
        System.out.println(v);
        System.out.println(w.getDeviation(v));
        System.out.println(w1.getDeviation(v));
        System.out.println(w2.getDeviation(v));
        System.out.println(w3.getDeviation(v));
    } */

}
