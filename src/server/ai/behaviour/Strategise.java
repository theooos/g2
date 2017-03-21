package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.AIConstants;
import server.ai.decision.LoadoutHandler;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

import java.util.ArrayList;
import java.util.Random;

import static server.ai.decision.AIConstants.*;

/**
 * This behaviour allows an AI-controlled player to decide which attack strategy they
 * will use against a pre-targeted player, based on its {@link LoadoutHandler}
 * and how far away the targeted player is. The correct decision may be discarded
 * because of random error.
 * <p>
 * Use of this behaviour is reserved for circumstances where the targeted player
 * is in direct line-of-sight of the AI-controlled player.
 *
 * Created by Rhys on 3/11/17.
 */
public class Strategise extends PlayerTask {

    private Random gen;
    private LoadoutHandler loadout;
    private ArrayList<Strategy> strategyList;
    private Strategy chosenStrategy;
    private enum Strategy{
        GIVE_UP,
        SNIPE,
        BUM_RUSH,
        SPRAY_N_PRAY
    }

    /**
     * Constructs a Strategise behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel The game-related intelligence the behaviour uses to make decisions.
     * @param brain The brain of the AI player that will be exhibiting this behaviour.
     * @param ldh   The AI Player's load-out handler, used for verifying weapon accessibility
     *              and equipping desired weapons.
     */
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
        return (brain.getEmotion() != PlayerBrain.EmotionalState.BORED);
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
        } else {
            chosenStrategy = Strategy.GIVE_UP;
        }

        // But will the player make the correct decision?
        if (gen.nextDouble() < AIConstants.CHANCE_STRATEGIC_ERR) {
            chosenStrategy = strategyList.get(gen.nextInt(strategyList.size()));
        }

        // Act upon selected strategy.
        float maxRange = 0;
        float targetRange = 0;
        double freq = 1;

        switch (chosenStrategy){
            case GIVE_UP:
                brain.setStrategy("ShiftPhase");
                break;

            case SNIPE:
                loadout.equipSniper();
                freq = 1.5 * intel.ent().getActiveWeapon().getRefireTime();
                maxRange = SNIPER_CEIL;
                targetRange = SMG_CEIL;
                brain.setStrategy("Attack");
                break;

            case BUM_RUSH:
                loadout.equipSMG();
                maxRange = SMG_CEIL;
                targetRange = SHOTGUN_OPT;
                brain.setStrategy("Attack");
                break;

            case SPRAY_N_PRAY:
                loadout.equipShotgun();
                freq = intel.ent().getActiveWeapon().getRefireTime();
                freq *= 1.2;
                maxRange = SHOTGUN_CEIL;
                targetRange = SHOTGUN_OPT;
                brain.setStrategy("Attack");
                break;
        }
        ((Attack)brain.getBehaviour("Attack")).setParameters(maxRange, targetRange, (int) Math.floor(freq));
        brain.executeStrategy();
        end();
    }
}
