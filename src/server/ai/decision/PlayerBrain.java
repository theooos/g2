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
 *
 * Created by Rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    public enum EmotionalState{
        INTIMIDATED,
        VENGEFUL,
        IRRITATED,
        AGGRESSIVE,
        DETERMINED,
        BORED,
        AT_REST}

    private EmotionalState curEmotion;      // Holds the player's current emotional state.
    private EmotionalState newEmotion;      // Holds the player's upcoming emotional state, pending reaction times.

    private Sequence flee;
    private Sequence stalk;
    private Sequence hunt;
    private Task currentStrategy;

    private PlayerIntel intel;
    private Random gen;
    private Feel feel;
    private LoadoutHandler loadout;

    private int strategiseDelay;    // How long the player must wait before being allowed to re-strategise.
    private int phaseShiftDelay;    // How long the player must wait before being allowed to phase-shift.
    private int reactionDelay;      // How long the player must wait before reacting to a change in emotion.
    private double stress;          // Multiplies random error in several behaviours to allow variation between emotions.

    /**
     * Constructs a player's brain - the core decision-maker behind all
     * of an AI-controlled player's actions.
     *
     * @param intel the object the brain will use for making decisions based
     *              on the game-world.
     */
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

    /**
     * Constructs all generic and player-only behaviours, and adds them to
     * the behaviour set.
     */
    @Override
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
        behaviours.addBehaviour(new Fetch(intel, this), "Fetch");
    }

    /**
     * Arranges behaviours into sequences for simpler use.
     */
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

        this.stalk = new Sequence(intel, this);
        stalk.add(behaviours.getBehaviour("FindPath"));
        stalk.add(behaviours.getBehaviour("Travel"));
    }

    /**
     * Calls upon the brain to analyse the situation and make the parent player
     * perform an appropriate action.
     */
    @Override
    public void doSomething() {

        tick();

        // Do nothing if dead.
        if (!intel.ent().isAlive()) {
            newEmotion = AT_REST;
            return;
        }

        // Decide emotion.
        feel.doFinal();

        // React, if necessary.
        if (curEmotion != newEmotion && reactionDelay == 0) respondToEmotion();

        switch (curEmotion) {

            case INTIMIDATED:
                if (behaviours.getBehaviour("Fetch").isRunning()){
                    behaviours.getBehaviour("Fetch").doAction();
                }
                else {
                    flee.doAction();
                }
                if (flee.hasFinished() || behaviours.getBehaviour("Fetch").hasFinished()){
                    curEmotion = AT_REST;   // Force more action if still intimidated next tick.
                }
                break;

            case IRRITATED:
                behaviours.getBehaviour("Swat").doAction();
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
                stalk.doAction();
                if (stalk.hasFinished()) resetBehaviours();     // Repeat if necessary, though highly unlikely.
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
     *
     * @param newEmotion the upcoming emotional state.
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
    private void respondToEmotion() {

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

                // If there's a power up available and chance allows:
                if (check.doCheck(Check.CheckMode.HEALTH_UP_VIABLE) &&
                        gen.nextDouble() <= AIConstants.CHANCE_PURSUE_HEALTH) {
                    behaviours.getBehaviour("Fetch").start();
                }
                else {
                    flee.start();
                }
                break;

            case IRRITATED:
                this.stress = STRESS_IRRITATED;
                behaviours.getBehaviour("Swat").start();
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
                }
                else {
                    this.stress = STRESS_DETERMINED;
                }
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
                intel.setTargetLocation(intel.getRelevantEntity().getPos());
                stalk.start();
                break;

            default:
                this.stress = STRESS_BORED;
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
                hunt.start();
                break;
        }

        curEmotion = newEmotion;

    }

    /**
     * @return the player's current stress level.
     */
    public double getStressLevel(){
        return stress;
    }

    /**
     * Sets the attacking strategy to be followed.
     *
     * @param strategy the chosen attack strategy.
     */
    public void setStrategy(String strategy) {
        this.currentStrategy = behaviours.getBehaviour(strategy);
    }

    /**
     * Wraps the Check object to allow objects without access to make complex checks.
     *
     * @param mode the desired check-mode.
     * @return     the result of the check.
     */
    public boolean performCheck(Check.CheckMode mode){
        return check.doCheck(mode);
    }

    /**
     * Makes the chosen strategy perform its starting logic.
     */
    public void executeStrategy(){
        currentStrategy.start();
    }

    /**
     * To be called by any method that makes the player phase-shift, in
     * order to reset the phase-shift delay timer.
     */
    public void shiftedPhase(){
        this.phaseShiftDelay = -(AIConstants.PHASE_SHIFT_DELAY);
    }

    /**
     * Checks whether or not the phase-shift delay timer has reached zero,
     * indicating that phase-shift is permitted.
     * @return true if phase-shift is permitted.
     */
    public boolean phaseShiftAuth(){
        return phaseShiftDelay == 0;
    }

    /**
     * Advances all timers, if they haven't already stopped.
     */
    private void tick(){
        if (phaseShiftDelay != 0) phaseShiftDelay++;
        if (reactionDelay != 0) reactionDelay++;
        if (strategiseDelay != 0) strategiseDelay++;
    }
}
