package server.game;

/**
 * Created by peran on 2/13/17.
 * A slow firing, close range weapon with multiple pellets
 */
public class WeaponShotgun extends Weapon {
    public WeaponShotgun() {
        super();
        name = "Shotgun";
        shotType = new DistDropOffProjectile(40,30,10,new Vector2(0,0), new Vector2(0,0), 10, 0, null, 0);
        numProjectiles = 7;
        //the max inaccuracy from recoil alone
        maxRecoil = 5;
        //base degree of inaccuracy
        accuracy = 20;
        //degrees of inaccuracy added depending on weapon type
        //loads for snipers, less for fully auto guns
        bloomPerShot = 5;
        //how many degrees of accuracy is recovered per tick
        recoilRecovery = .1;
        //the speed the weapon cools down
        coolDownRate = 0.3;
        //the amount of heat each firing of the weapon takes up
        heatPerShot = 25;
        //min time after firing a shot till the gun can fire again
        refireTime = 15;
        //whether the player can hold down the trigger
        fullyAuto = false;
    }
}
