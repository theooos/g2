package server.game;

import objects.Sendable;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by peran on 01/02/17.
 * A default weapon class with example stats
 */
public abstract class Weapon implements Sendable{
    int accuracy;
    int maxRecoil;
    double recoilRecovery;
    private double currentRecoil;
    private int maxHeat;
    int bloomPerShot;
    int heatPerShot;
    private double currentHeat;
    double coolDownRate;
    Projectile shotType;
    int numProjectiles;
    private Random rand;
    int refireTime;
    private int refireDelay;
    boolean fullyAuto;
    String name;

    /**
     * An example weapon class
     */
    Weapon() {
        name = "Default";
        //with a cooldown rate of 1, it'll take 2s for a full reload
        maxHeat = 120;
        currentHeat = 0;
        shotType = new Projectile(100,100,20,new Vector2(0,0), new Vector2(0,0), 100, 0, null, 0);
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

    /**
     * Checks to see whether the max heat and refire delay won't restrict the weapon to fire
     * @return if the weapon can fire or not
     */
    boolean canFire() {
        return (maxHeat - currentHeat >= heatPerShot) && refireDelay <= 0;
    }

    ArrayList<Projectile> getShots(Player player) {
        ArrayList<Projectile> ps = new ArrayList<>();
        for (int i = 0; i < numProjectiles; i++) {
            Projectile p = shotType.clone();
            p.setDir(getDeviation(player.getDir()));
            p.setPos(player.getPos());
            p.setPhase(player.getPhase());
            p.setPlayer(player);
            p.setTeam(player.getTeam());
            ps.add(p);
        }
        currentRecoil += bloomPerShot;
        if (currentRecoil > maxRecoil) currentRecoil = maxRecoil;
        currentHeat += heatPerShot;
        if (currentHeat > maxHeat) {
            currentHeat = maxHeat;
        }
        refireDelay = refireTime;
        if (!isFullyAuto()) {
            player.setFiring(false);
        }
        return ps;
    }

    /**
     * Adds inaccuracy to the weapons
     * @param v
     * @return
     */
    private Vector2 getDeviation(Vector2 v) {
        //x' = xcosf - ysinf
        //y' = ycosf - xsinf
        //get the angle from the vector
        double ang = Math.atan(v.getX()/v.getY());
        if (Double.isInfinite(ang)) {
            ang = 0;
        } else if (v.getY() < 0) {
            ang += Math.PI;
        }
        //add the inaccuracy
        ang += Math.toRadians(rand.nextInt(2*(int)(accuracy+currentRecoil))-(accuracy+currentRecoil));
        float newX = (float)(Math.sin(ang));
        float newY = (float)(Math.cos(ang));

        //return a new vector
        return (new Vector2(newX, newY)).normalise();
    }

    public boolean isFullyAuto() {
         return fullyAuto;
    }

    public String toString() {
         return name;
    }

    double getHeat() {
         return currentHeat;
    }

    public int getMaxHeat() {
         return maxHeat;
    }

    public int getRefireTime(){
        return refireTime;
    }

    void setCurrentHeat(int heat) {
        this.currentHeat = heat;
    }

}
