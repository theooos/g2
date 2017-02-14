package server.game;

/**
 * Created by peran on 2/13/17.
 * A slow firing, high damage weapon type
 */
public class WeaponSniper extends Weapon {
    public WeaponSniper() {
        super();
        shotType = new Projectile(70,700,20,new Vector2(0,0), new Vector2(0,0), 30, 0, 0, 0);
        numProjectiles = 1;
        //the max inaccuracy from recoil alone
        maxRecoil = 80;
        //base degree of inaccuracy
        accuracy = 2;
        //degrees of inaccuracy added depending on weapon type
        //loads for snipers, less for fully auto guns
        bloomPerShot = 40;
        //how many degrees of accuracy is recovered per tick
        recoilRecovery = 1;
        //the speed the weapon cools down
        coolDownRate = 0.3;
        //the amount of heat each firing of the weapon takes up
        heatPerShot = 30;
        //min time after firing a shot till the gun can fire again
        refireTime = 20;
        //whether the player can hold down the trigger
        fullyAuto = false;
    }
}