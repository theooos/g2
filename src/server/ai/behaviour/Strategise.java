package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.*;

/**
 * Allows the player to decide which attack strategy they will use, based on
 * their load-out, circumstances and some random chance.
 * Created by Rhys on 3/11/17.
 */
public class Strategise extends PlayerTask {

    public Strategise(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (brain.getEmotion() != AIBrain.EmotionalState.BORED);
    }

    @Override
    public void doAction() {
        brain.resetBehaviours();

        float distance = intel.ent().getPos().getDistanceTo(intel.getRelevantEntity().getPos());

        // Waiting on ability to calculate range of weapons.
        // Very basic strategising in the meantime.
        /*if (haveSniper()) {
            System.out.println("Sniping!");
            brain.setStrategy("Fire");
            equipSniper();
            brain.getBehaviour("Fire").start();
        } else {
            System.out.println("Sprayin'!");*/
            brain.setStrategy("SprayNPray");
            equipShotgun();
            brain.getBehaviour("SprayNPray").start();

        /*}*/

        end();
    }

    /**
     * Checks whether or not the player possesses a sniper.
     * @return true if the player has a sniper.
     */
    private boolean haveSniper(){
        return (intel.ent().getWeapon1() instanceof WeaponSniper) ||
                (intel.ent().getWeapon2() instanceof WeaponSniper);
    }

    /**
     * Sets the player's current weapon to the sniper.
     * Assumes prior confirmation that the player does indeed possess a sniper.
     */
    private void equipSniper(){
        if (!(intel.ent().getActiveWeapon() instanceof WeaponSniper)) {
            intel.ent().toggleWeapon();
        }
    }

    /**
     * Checks whether or not the player possesses an SMG.
     * @return true if the player has a SMG.
     */
    private boolean haveSMG(){
        return (intel.ent().getWeapon1() instanceof WeaponSMG) ||
                (intel.ent().getWeapon2() instanceof WeaponSMG);
    }

    /**
     * Checks whether or not the player possesses a shotgun.
     * @return true if the player has a shotgun.
     */
    private boolean haveShotgun(){
        return (intel.ent().getWeapon1() instanceof WeaponShotgun) ||
                (intel.ent().getWeapon2() instanceof WeaponShotgun);
    }

    /**
     * Sets the player's current weapon to the shotgun.
     * Assumes prior confirmation that the player does indeed possess a shotgun.
     */
    private void equipShotgun() {
        if (!(intel.ent().getActiveWeapon() instanceof WeaponShotgun)) {
            intel.ent().toggleWeapon();
        }
    }

}
