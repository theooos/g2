package server.ai.behaviour;

import server.ai.AIBrain;
import server.ai.Intel;
import server.game.*;

/**
 * Created by rhys on 3/11/17.
 */
public class Strategise extends Task {

    public Strategise(Intel intel, AIBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions() {
        return (brain.getEmotion() != AIBrain.EmotionalState.BORED);
    }

    @Override
    public void doAction() {

    }

    private boolean haveSniper(){
        AIPlayer ent = (AIPlayer) intel.ent();
        return (ent.getWeapon1() instanceof WeaponSniper) || (ent.getWeapon2() instanceof WeaponSniper);
    }

    private boolean haveSMG(){
        AIPlayer ent = (AIPlayer) intel.ent();
        return (ent.getWeapon1() instanceof WeaponSMG) || (ent.getWeapon2() instanceof WeaponSMG);
    }

    private boolean haveShotgun(){
        AIPlayer ent = (AIPlayer) intel.ent();
        return (ent.getWeapon1() instanceof WeaponShotgun) || (ent.getWeapon2() instanceof WeaponShotgun);
    }

}
