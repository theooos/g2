package server.game;

/**
 * Created by peran on 2/13/17.
 * A fast firing, low damage weapon type
 */
class WeaponSMG extends Weapon {
    WeaponSMG() {
        super();
        shotType = new Projectile(15,300,10,new Vector2(0,0), new Vector2(0,0), 20, 0, null, 0);
        numProjectiles = 1;
        //the max inaccuracy from recoil alone
        maxRecoil = 25;
        //base degree of inaccuracy
        accuracy = 5;
        //degrees of inaccuracy added depending on weapon type
        //loads for snipers, less for fully auto guns
        bloomPerShot = 1;
        //how many degrees of accuracy is recovered per tick
        recoilRecovery = .1;
        //the speed the weapon cools down
        coolDownRate = 0.05;
        //the amount of heat each firing of the weapon takes up
        heatPerShot = 3;
        //min time after firing a shot till the gun can fire again
        refireTime = 10;
        //whether the player can hold down the trigger
        fullyAuto = true;
    }
}
