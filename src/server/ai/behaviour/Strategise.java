package server.ai.behaviour;

import objects.PhaseObject;
import server.ai.AIBrain;
import server.ai.PlayerTask;
import server.ai.decision.LoadoutHandler;
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
    private LoadoutHandler loadout;
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

    public Strategise(PlayerIntel intel, PlayerBrain brain, LoadoutHandler ldh){
        super(intel, brain);
        this.loadout = ldh;
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
            if (loadout.haveSniper()){
                chosenStrategy = Strategy.SNIPE;
            }
            else {
                chosenStrategy = Strategy.BUM_RUSH;
            }
        }
        else if (distance > SHOTGUN_CEIL) {
            if (loadout.haveSMG()) {
                chosenStrategy = Strategy.BUM_RUSH;
            }
            else {
                chosenStrategy = Strategy.SPRAY_N_PRAY;
            }
        }
        else if (distance > SHOTGUN_OPT) {
            if (loadout.haveShotgun()) {
                chosenStrategy = Strategy.SPRAY_N_PRAY;
            } else {
                chosenStrategy = Strategy.BUM_RUSH;
            }
        }
        else {
            if (loadout.haveShotgun()){
                chosenStrategy = Strategy.RETREAT_WITH_SHOTGUN;
            }
            else {
                chosenStrategy = Strategy.RETREAT_WITH_SMG;
            }
        }

        // But will the player make the correct decision?
        /*if (gen.nextDouble() > 0.7) {
            chosenStrategy = strategyList.get(gen.nextInt(strategyList.size()));
        }*/


        // Act upon selected strategy.
        switch (chosenStrategy){
            case GIVE_UP:
                System.out.println("Chosen strategy: Not worth the hassle.");
                brain.setStrategy("ShiftPhase");
                break;

            case SNIPE:
                System.out.println("Chosen strategy: Snipe.");
                loadout.equipSniper();
                int freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.5;
                ((Attack)brain.getBehaviour("Attack")).setParameters(SNIPER_CEIL, SMG_CEIL, freq);
                brain.setStrategy("Attack");
                break;

            case BUM_RUSH:
                System.out.println("Chosen strategy: Bum Rush.");
                loadout.equipSMG();
                freq = 1; // Dummy value for freq. SMGs are semi-automatic.
                ((Attack)brain.getBehaviour("Attack")).setParameters(SMG_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Attack");
                break;

            case SPRAY_N_PRAY:
                System.out.println("Chosen strategy: SprayNPray.");
                loadout.equipShotgun();
                freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.2;
                ((Attack)brain.getBehaviour("Attack")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Attack");
                break;

            case RETREAT_WITH_SHOTGUN:
                System.out.println("Chosen strategy: Retreat.");
                loadout.equipShotgun();
                freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.2;
                ((Retreat)brain.getBehaviour("Retreat")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Retreat");
                break;

            default:
                System.out.println("Chosen strategy: Retreat.");
                loadout.equipSMG();
                freq = 1;
                ((Retreat)brain.getBehaviour("Retreat")).setParameters(SHOTGUN_CEIL, SHOTGUN_OPT, freq);
                brain.setStrategy("Retreat");
                break;
        }

        brain.executeStrategy();
        end();
    }
}
