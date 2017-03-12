package server.ai.decision;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.Task;
import server.ai.behaviour.*;

import java.util.Random;

/**
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    private Sequence rage;
    private Sequence chase;
    private Sequence hunt;

    private PlayerIntel intel;
    private Random gen;
    private int tickCount;
    private Task currentStrategy;
    private int stress;

    public PlayerBrain(PlayerIntel intel) {
        super(intel);
        this.intel = intel;
        this.gen = new Random();
        tickCount = 0;
    }

    protected void constructBehaviours(){
        super.constructBehaviours();
        behaviours.addBehaviour(new Travel(intel, this), "Travel");
        behaviours.addBehaviour(new Strategise(intel, this), "Strategise");
        behaviours.addBehaviour(new Fire(intel, this), "Fire");
        behaviours.addBehaviour(new SprayNPray(intel, this), "SprayNPray");
    }
    @Override
    protected void configureBehaviours() {
        this.hunt = new Sequence(intel, this);
        hunt.add(behaviours.getBehaviour("Wander"));
        hunt.add(behaviours.getBehaviour("FindPath"));
        hunt.add(behaviours.getBehaviour("Travel"));
    }

    @Override
    public void doSomething() {
        // Perform checks.
        boolean lowHealth = check.doCheck(Check.CheckMode.HEALTH);
        boolean playerNear = check.doCheck(Check.CheckMode.PROXIMITY_PLY);

        // Decide emotion.
        feel.setParameters(lowHealth, playerNear);
        feel.doFinal();

        // Decide how to behave...
        if (curEmotion == EmotionalState.INTIMIDATED){
            // IMPLEMENT THE CHECK.
            boolean retaliationViable = check.doCheck(Check.CheckMode.COUNTER_ATTACK_VIABLE);
            if (retaliationViable) {
                if (!behaviours.getBehaviour("Retaliate").isRunning()){
                    behaviours.getBehaviour("Retaliate").start();
                }
                    behaviours.getBehaviour("Retaliate").doAction();
            } else {
                if (!behaviours.getBehaviour("Escape").isRunning()) {
                    behaviours.getBehaviour("Escape").start();
                }
                behaviours.getBehaviour("Escape").doAction();
            }
         }

        else if (curEmotion == EmotionalState.AGGRESSIVE) {

            // Give the AI a 50% chance of rethinking strategy after at least 2 seconds.
            if ((tickCount/60) >= 2) {
                if (gen.nextDouble() >= 0.5) {
                    System.out.println("Rethinking strategy.");
                    behaviours.getBehaviour("Strategise").run();
                    tickCount = 0;
                }
            }
            // Compute/re-compute travel path if the target has moved since the last tick.
            if (check.doCheck(Check.CheckMode.TARGET_MOVED)) {
                intel.setTargetLocation(intel.getTargetPlayer().getPos());
            }

            // Execute strategy.
            tickCount++;
            currentStrategy.doAction();
        }

        else if (curEmotion == EmotionalState.BORED) {
            hunt.doAction();
        }

    }

    protected void handleEmotion() {
        if (curEmotion == EmotionalState.INTIMIDATED) {
            System.out.println("Player "+ intel.ent().getID() + " is now intimidated.");
            ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
            this.stress = 80;
        }
        else if (curEmotion == EmotionalState.AGGRESSIVE) {
            System.out.println("Player "+ intel.ent().getID() + " is now aggressive.");
            this.stress = 50;
            ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(true);
            behaviours.getBehaviour("Strategise").run();
        }
        else if (curEmotion == EmotionalState.BORED) {
            System.out.println("Player "+ intel.ent().getID() + " is now bored.");
            this.stress = 0;
            ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
            hunt.start();
        }
    }

    public int getStressLevel(){
        return stress;
    }

    public void setStrategy(String strategy) {
        this.currentStrategy = behaviours.getBehaviour(strategy);
    }
}
