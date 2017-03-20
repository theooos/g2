package server.ai.decision;

import server.ai.AIBrain;
import server.ai.Task;
import server.ai.behaviour.*;

import java.util.Random;

import static server.ai.decision.AIConstants.*;
import static server.ai.decision.PlayerBrain.EmotionalState.*;

/**
 * Represents the brain of an AI-controlled player, making decisions on the
 * player's behalf while taking the player's situation and surroundings into
 * account.
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    public enum EmotionalState{
        INTIMIDATED,
        IRRITATED,
        VENGEFUL,
        AGGRESSIVE,
        DETERMINED,
        BORED,
        AT_REST}

    private EmotionalState curEmotion;
    private EmotionalState newEmotion;

    private Sequence flee;
    private Sequence rage;
    private Sequence chase;
    private Sequence hunt;
    private Task currentStrategy;

    private PlayerIntel intel;
    private Random gen;
    private LoadoutHandler loadout;

    private int strategiseDelay;
    private int phaseShiftDelay;
    private int reactionDelay;
    private double stress;

    public PlayerBrain(PlayerIntel intel) {
        super(intel);
        this.intel = intel;
        this.feel = new Feel(this, check, intel);
        this.gen = new Random();
        this.loadout = new LoadoutHandler(intel.ent());
        this.strategiseDelay = 0;
        this.phaseShiftDelay = 0;
        this.reactionDelay = 0;
        this.stress = STRESS_BORED;
        this.curEmotion = AT_REST;
    }

    protected void constructBehaviours(){
        super.constructBehaviours();
        behaviours.addBehaviour(new Travel(intel, this), "Travel");
        behaviours.addBehaviour(new Strategise(intel, this, loadout), "Strategise");
        behaviours.addBehaviour(new Fire(intel, this), "Fire");
        behaviours.addBehaviour(new ShiftPhase(intel, this), "ShiftPhase");
        behaviours.addBehaviour(new QuickMove(intel,this), "QuickMove");
        behaviours.addBehaviour(new ForceShiftPhase(intel, this), "ForceShiftPhase");
        behaviours.addBehaviour(new Attack(intel, this), "Attack");
        behaviours.addBehaviour(new Swat(intel, this, loadout), "Swat");
    }
    @Override
    protected void configureBehaviours() {
        this.hunt = new Sequence(intel, this);
        hunt.add(behaviours.getBehaviour("Wander"));
        hunt.add(behaviours.getBehaviour("FindPath"));
        hunt.add(behaviours.getBehaviour("Travel"));

        this.flee = new Sequence(intel, this);
        flee.add(behaviours.getBehaviour("ForceShiftPhase"));
        flee.add(behaviours.getBehaviour("LocateCover"));
        flee.add(behaviours.getBehaviour("FindPath"));
        flee.add(behaviours.getBehaviour("Travel"));
    }

    @Override
    public void doSomething() {

        tick();

        // Do nothing if dead.
        if (!intel.ent().isAlive()) {
            newEmotion = AT_REST;
            System.out.println("Playersfsssw dead.");
            return;
        }

        // Decide emotion.
        feel.doFinal();

        // React, if necessary.
        if (curEmotion != newEmotion && reactionDelay == 0) respondToEmotion();

        switch (curEmotion) {

            case INTIMIDATED:
                flee.doAction();
                if (flee.hasFinished()) intel.setEscaped(true);
                break;

            case IRRITATED:
                behaviours.getBehaviour("Swat").doAction();
                break;

            case VENGEFUL:
                break;

            case AGGRESSIVE:

                // Give the AI a 50% chance of rethinking strategy after at least 2 seconds.
                if (strategiseDelay == 0) {
                    if (gen.nextDouble() < CHANCE_STRATEGIC_RETHINK) {
                        behaviours.getBehaviour("Strategise").run();
                        strategiseDelay = STRATEGY_RETHINK_DELAY;
                    }
                }

                // Execute strategy.
                currentStrategy.doAction();

                if (currentStrategy.hasFinished()) {
                    behaviours.resetAll();
                    behaviours.getBehaviour("Strategise").run();
                }
                break;

            case DETERMINED:

                break;

            default:

                // Gives player option to change phase - but no more than once per 2 seconds.
                if (gen.nextDouble() < CHANCE_PHASE_SHIFT) {
                    behaviours.getBehaviour("ShiftPhase").run();
                    hunt.reset();
                    hunt.start();
                }

                hunt.doAction();
                break;
        }

    }

    /**
     * @return the current emotional state of the AI Unit this Brain belongs to.
     */
    public EmotionalState getEmotion() {
        return curEmotion;
    }

    /**
     * Checks for a change in emotional state, starting the response process in the
     * case that there has been a change.
     * @param newEmotion - The upcoming emotional state.
     */
    public void setEmotion(EmotionalState newEmotion) {
        if (newEmotion != this.newEmotion) {
            this.newEmotion = newEmotion;
            handleEmotion();
        }
    }

    /**
     * Sets this brains reaction time appropriately for the new emotion.
     */
    private void handleEmotion() {

        switch (newEmotion) {
            case INTIMIDATED:
                this.reactionDelay = -REACTION_TIME_LOW;
                break;

            case IRRITATED:
                this.reactionDelay = -REACTION_TIME_AVG;
                break;

            default:
                this.reactionDelay = -REACTION_TIME_HIGH;
        }
    }


    /**
     * Determines how the AI Player behaves when it experiences a change in emotion.
     */
    protected void respondToEmotion() {

        behaviours.resetAll();
        intel.resetIntel();
        intel.loadRelevantEntity();

        // If the player isn't aggressive, stop firing the SMG!
        if (newEmotion != EmotionalState.AGGRESSIVE){
            intel.ent().setFiring(false);
        }

        switch (newEmotion) {

            case INTIMIDATED:
                this.stress = STRESS_INTIMIDATED;
                flee.start();
                break;

            case IRRITATED:
                this.stress = STRESS_IRRITATED;
                behaviours.getBehaviour("Swat").start();
                break;

            case VENGEFUL:
                this.stress = STRESS_VENGEFUL;
                break;

            case AGGRESSIVE:
                if (curEmotion == BORED) {
                    this.stress = STRESS_AGGRESSIVE_FROM_BORED;
                }
                else {
                    this.stress = STRESS_AGGRESSIVE;
                }
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(true);
                behaviours.getBehaviour("Strategise").run();
                break;

            case DETERMINED:
                if (curEmotion == BORED) {
                    this.stress = STRESS_DETERMINED_FROM_BORED;
                } else {
                    this.stress = STRESS_DETERMINED;
                }
                break;

            default:
                this.stress = STRESS_BORED;
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
                hunt.start();
                break;
        }

        curEmotion = newEmotion;

    }

    public double getStressLevel(){
        return stress;
    }

    public void setStrategy(String strategy) {
        this.currentStrategy = behaviours.getBehaviour(strategy);
    }

    public boolean performCheck(Check.CheckMode mode){
        return check.doCheck(mode);
    }

    public void executeStrategy(){
        currentStrategy.start();
    }

    public void shiftedPhase(){
        this.phaseShiftDelay = -(AIConstants.PHASE_SHIFT_DELAY);
    }

    public boolean phaseShiftAuth(){
        return phaseShiftDelay == 0;
    }

    private void tick(){
        if (phaseShiftDelay != 0) phaseShiftDelay++;
        if (reactionDelay != 0) reactionDelay++;
        if (strategiseDelay != 0) strategiseDelay++;
    }
}
