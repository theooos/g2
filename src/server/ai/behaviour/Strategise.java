package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.*;

/**
 * Created by rhys on 3/11/17.
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
        if (intel == null) System.out.println("Intel is null.");
        if (intel.ent() == null) System.out.println("AI Player is null.");
        if (intel.getTargetPlayer() == null) System.out.println("Target player is null.");

        float distance = intel.ent().getPos().getDistanceTo(intel.getTargetPlayer().getPos());

        // Waiting on ability to calculate range of weapons.
        // Very basic strategising in the meantime.
        if (haveSniper()) {
            brain.setStrategy("Fire");
            equipSniper();
            brain.getBehaviour("Fire").start();
        } else {
            brain.setStrategy("SprayNPray");
            equipShotgun();
            brain.getBehaviour("SprayNPray").start();

        }

        end();
    }

    private boolean haveSniper(){
        return (intel.ent().getWeapon1() instanceof WeaponSniper) ||
                (intel.ent().getWeapon2() instanceof WeaponSniper);
    }

    private void equipSniper(){
        if (!(intel.ent().getActiveWeapon() instanceof WeaponSniper)) {
            intel.ent().toggleWeapon();
        }
    }

    private boolean haveSMG(){
        return (intel.ent().getWeapon1() instanceof WeaponSMG) ||
                (intel.ent().getWeapon2() instanceof WeaponSMG);
    }

    private boolean haveShotgun(){
        return (intel.ent().getWeapon1() instanceof WeaponShotgun) ||
                (intel.ent().getWeapon2() instanceof WeaponShotgun);
    }

    private void equipShotgun() {
        if (!(intel.ent().getActiveWeapon() instanceof WeaponShotgun)) {
            intel.ent().toggleWeapon();
        }
    }

}
