package server.ai.behaviour;

import objects.PhaseObject;
import server.ai.AIBrain;
import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Allows the player to decide which attack strategy they will use, based on
 * their load-out, circumstances and some random chance.
 * Created by Rhys on 3/11/17.
 */
public class Strategise extends PlayerTask {

    private final float SNIPER_CEIL = 600;
    private final float SMG_CEIL = 400;
    private final float SHOTGUN_CEIL = 200;
    private final float SHOTGUN_OPT = 75;
    private Random gen;
    private ArrayList<Strategy> strategyList;
    private Strategy chosenStrategy;
    private enum Strategy{
        GIVE_UP,
        SNIPE,
        BUM_RUSH,
        SPRAY_N_PRAY,
        RETREAT_WITH_SHOTGUN,
        RETREAT_WITH_SMG
    }

    public Strategise(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        this.gen = new Random();
        strategyList = new ArrayList<>();
        for (Strategy s : Strategy.values()){
            strategyList.add(s);
        }
    }

    @Override
    public boolean checkConditions() {
        return (brain.getEmotion() != AIBrain.EmotionalState.BORED);
    }

    @Override
    public void doAction() {

        brain.resetBehaviours();
        float distance = intel.ent().getPos().getDistanceTo(intel.getRelevantEntity().getPos());

        // Determine the "best" strategy to follow.
        if (distance > SNIPER_CEIL){
            chosenStrategy = Strategy.GIVE_UP;
        }
        else if (distance > SMG_CEIL) {
            if (haveSniper()){
                chosenStrategy = Strategy.SNIPE;
            }
            else {
                chosenStrategy = Strategy.BUM_RUSH;
            }
        }
        else if (distance > SHOTGUN_CEIL) {
            if (haveSMG()) {
                chosenStrategy = Strategy.BUM_RUSH;
            }
            else {
                chosenStrategy = Strategy.SPRAY_N_PRAY;
            }
        }
        else if (distance > SHOTGUN_OPT) {
            if (haveShotgun()) {
                chosenStrategy = Strategy.SPRAY_N_PRAY;
            } else {
                chosenStrategy = Strategy.BUM_RUSH;
            }
        }
        else {
            if (haveShotgun()){
                chosenStrategy = Strategy.RETREAT_WITH_SHOTGUN;
            }
            else {
                chosenStrategy = Strategy.RETREAT_WITH_SMG;
            }
        }

        // But will the player make the correct decision?
        if (gen.nextDouble() > 0.7) {
            chosenStrategy = strategyList.get(gen.nextInt(strategyList.size()));
        }


        // Act upon selected strategy.
        switch (chosenStrategy){
            case GIVE_UP:
                System.out.println("Chosen strategy: Not worth the hassle.");
                brain.setStrategy("ShiftPhase");
                break;

            case SNIPE:
                System.out.println("Chosen strategy: Snipe.");
                equipSniper();
                int freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.5;
                ((Attack)brain.getBehaviour("Attack")).setParameters(SNIPER_CEIL, SMG_CEIL, freq);
                brain.setStrategy("Attack");
                break;

            case BUM_RUSH:
                System.out.println("Chosen strategy: Bum Rush.");
                equipSMG();
                freq = 1; // Dummy value for freq. SMGs are semi-automatic.
                ((Attack)brain.getBehaviour("Attack")).setParameters(SMG_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Attack");
                break;

            case SPRAY_N_PRAY:
                System.out.println("Chosen strategy: SprayNPray.");
                equipShotgun();
                freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.2;
                ((Attack)brain.getBehaviour("Attack")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Attack");
                break;

            case RETREAT_WITH_SHOTGUN:
                System.out.println("Chosen strategy: Retreat.");
                equipShotgun();
                freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.2;
                ((Retreat)brain.getBehaviour("Retreat")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Retreat");
                break;

            default:
                System.out.println("Chosen strategy: Retreat.");
                equipSMG();
                freq = 1;
                ((Retreat)brain.getBehaviour("Retreat")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Retreat");
                break;
        }

        brain.executeStrategy();
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
     * Sets the player's current weapon to the SMG.
     * Assumes prior confirmation that the player does indeed possess an SMP.
     */
    private void equipSMG(){
        if (!(intel.ent().getActiveWeapon() instanceof WeaponSMG)) {
            intel.ent().toggleWeapon();
        }
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
