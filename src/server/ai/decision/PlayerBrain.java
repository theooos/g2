package server.ai.decision;

import server.ai.AIBrain;
import server.ai.Task;
import server.ai.behaviour.*;

import java.util.Random;

/**
 * Represents the brain of an AI-controlled player, making decisions on the
 * player's behalf while taking the player's situation and surroundings into
 * account.
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    private Sequence flee;
    private Sequence rage;
    private Sequence chase;
    private Sequence hunt;
    private Task currentStrategy;

    private PlayerIntel intel;
    private Random gen;

    private int tickCount;
    private final int maxPSDelay=120;
    private int phaseShiftDelay;
    private int stress;
    private int reactionDelay;

    public PlayerBrain(PlayerIntel intel) {
        super(intel);
        this.intel = intel;
        this.gen = new Random();
        tickCount = 0;
        phaseShiftDelay = 0;
        reactionDelay = 0;
    }

    protected void constructBehaviours(){
        super.constructBehaviours();
        behaviours.addBehaviour(new Travel(intel, this), "Travel");
        behaviours.addBehaviour(new Strategise(intel, this), "Strategise");
        behaviours.addBehaviour(new Fire(intel, this), "Fire");
        behaviours.addBehaviour(new ShiftPhase(intel, this), "ShiftPhase");
        behaviours.addBehaviour(new QuickMove(intel,this), "QuickMove");
        behaviours.addBehaviour(new ForceShiftPhase(intel, this), "ForceShiftPhase");
        behaviours.addBehaviour(new Attack(intel, this), "Attack");
        behaviours.addBehaviour(new Retreat(intel, this), "Retreat");
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
        // Decide emotion.
        feel.doFinal();

        // React, if necessary.
        if (curEmotion != newEmotion && reactionDelay++ == 0) respondToEmotion();

        switch (curEmotion) {

            case INTIMIDATED:
                flee.doAction();
                break;

            case IRRITATED:
                break;

            case VENGEFUL:
                break;

            case AGGRESSIVE:
                // Give the AI a 50% chance of rethinking strategy after at least 2 seconds.
                if ((tickCount/60) >= 2) {
                    if (gen.nextDouble() >= 0.5) {
                        behaviours.getBehaviour("Strategise").run();
                        tickCount = 0;
                    }
                }
                // Compute/relevantEnt-compute travel path if the target has moved since the last tick.
                if (check.doCheck(Check.CheckMode.TARGET_MOVED)) {
                    intel.setTargetLocation(intel.getRelevantEntity().getPos());
                }

                // Execute strategy.
                tickCount++;
                currentStrategy.doAction();
                break;

            case DETERMINED:
                break;

            default:
                // Gives player option to change phase - but no more than once per 2 seconds.
                if (gen.nextDouble() > 0.99) {
                    behaviours.getBehaviour("ShiftPhase").run();
                    hunt.reset();
                    hunt.start();
                }
                hunt.doAction();
                break;
        }

        if (phaseShiftDelay++ < 0);
    }

    /**
     * Determines how the AI Player behaves when it experiences a change in emotion.
     */
    protected void handleEmotion() {

        switch (newEmotion) {
            case INTIMIDATED:
                this.reactionDelay = -8;
                break;

            case IRRITATED:
                this.reactionDelay = -9;
                break;

            default:
                this.reactionDelay = -10;
        }
    }


    /**
     * Determines how the AI Player behaves when it experiences a change in emotion.
     */
    protected void respondToEmotion() {

        behaviours.resetAll();
        curEmotion = newEmotion;

        // If the player isn't aggressive, stop firing the SMG!
        if (curEmotion != EmotionalState.AGGRESSIVE){
            intel.ent().setFiring(false);
        }

        switch (curEmotion) {

            case INTIMIDATED:
                System.out.println("Player "+ intel.ent().getID() + " is now intimidated.");
                this.stress = 80;
                flee.start();
                break;

            case IRRITATED:
                System.out.println("Player "+ intel.ent().getID() + " is now irritated.");
                this.stress = 60;
                break;

            case VENGEFUL:
                break;

            case AGGRESSIVE:
                System.out.println("Player "+ intel.ent().getID() + " is now aggressive.");
                this.stress = 50;
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(true);
                behaviours.getBehaviour("Strategise").run();
                break;

            case DETERMINED:
                break;

            default:
                System.out.println("Player "+ intel.ent().getID() + " is now bored.");
                this.stress = 0;
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
                hunt.start();
                break;
        }
    }

    public int getStressLevel(){
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
        this.phaseShiftDelay = -maxPSDelay;
    }

    public boolean phaseShiftAuth(){
        return phaseShiftDelay == 0;
    }
}
