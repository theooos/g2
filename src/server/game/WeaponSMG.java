package server.game;

/**
 * Created by peran on 2/13/17.
 * A fast firing, low damage weapon type
 */
public class WeaponSMG extends Weapon {

    /**
     * Creates a new fast firing low damage weapon
     */
    WeaponSMG() {
        super();
        name = "SMG";
        shotType = new Projectile(15,40,6,new Vector2(0,0), new Vector2(0,0), 15, 0, null, 0);
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
        coolDownRate = 0.5;
        //the amount of heat each firing of the weapon takes up
        heatPerShot = 8;
        //min time after firing a shot till the gun can fire again
        refireTime = 10;
        //whether the player can hold down the trigger
        fullyAuto = true;
    }
}
