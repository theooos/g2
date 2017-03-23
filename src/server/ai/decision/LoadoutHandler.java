package server.ai.decision;

import server.game.*;

/**
 * Contains useful methods for checking and equipping available weapons.
 *
 * Created by Rhys on 3/17/17.
 */
public class LoadoutHandler {

    private Weapon weapon1;
    private Weapon weapon2;
    private AIPlayer player;

    /**
     * Creates a LoadoutHandler object for the given AI player.
     *
     * @param player the player whose weapons this object will handle.
     */
    public LoadoutHandler(AIPlayer player) {
        this.player = player;
        this.weapon1 = player.getWeapon1();
        this.weapon2 = player.getWeapon2();
    }

    /**
     * Checks whether or not the player possesses a sniper.
     *
     * @return <CODE>true</CODE> if the player has a sniper.
     */
    public boolean haveSniper(){
        return (weapon1 instanceof WeaponSniper) || (weapon2 instanceof WeaponSniper);
    }

    /**
     * Sets the player's current weapon to the sniper.
     *
     * Assumes prior confirmation that the player does indeed possess a sniper.
     */
    public void equipSniper(){
        if (!(player.getActiveWeapon() instanceof WeaponSniper)) player.toggleWeapon();
    }

    /**
     * Checks whether or not the player possesses an SMG.
     *
     * @return true if the player has a SMG.
     */
    public boolean haveSMG(){
        return (weapon1 instanceof WeaponSMG) || (weapon2 instanceof WeaponSMG);
    }

    /**
     * Sets the player's current weapon to the SMG.
     *
     * Assumes prior confirmation that the player does indeed possess an SMP.
     */
    public void equipSMG(){
        if (!(player.getActiveWeapon() instanceof WeaponSMG)) player.toggleWeapon();
    }

    /**
     * Checks whether or not the player possesses a shotgun.
     *
     * @return <CODE>true</CODE> if the player has a shotgun.
     */
    public boolean haveShotgun(){
        return (weapon1 instanceof WeaponShotgun) || (weapon2 instanceof WeaponShotgun);
    }

    /**
     *
     * Sets the player's current weapon to the shotgun.
     * Assumes prior confirmation that the player does indeed possess a shotgun.
     */
    public void equipShotgun() {
        if (!(player.getActiveWeapon() instanceof WeaponShotgun)) player.toggleWeapon();
    }
}
