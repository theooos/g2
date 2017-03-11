package server.ai.decision;

import server.ai.AIBrain;
import server.ai.Intel;
import server.ai.behaviour.Sequence;

import java.util.Random;

/**
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    private Sequence flee;
    private Sequence rage;
    private Sequence attack;
    private Sequence chase;
    private Sequence hunt;

    private Random gen;
    private int tickCount;
    private Sequence currentStrategy;
    private int stress;

    public PlayerBrain(Intel intel) {
        super(intel);
        this.gen = new Random();
        tickCount = 0;
    }

    @Override
    protected void configureBehaviours() {
        this.flee = new Sequence(intel, this);
        flee.add(behaviours.getBehaviour("LocateCover"));
        flee.add(behaviours.getBehaviour("FindPath"));

        this.hunt = new Sequence(intel, this);
        hunt.add(behaviours.getBehaviour("Wander"));
        hunt.add(behaviours.getBehaviour("FindPath"));
        hunt.add(behaviours.getBehaviour("Travel"));
    }

    @Override
    public void doSomething() {
        // Perform checks.
        boolean lowHealth = check.doCheck(Check.CheckMode.HEALTH);
        boolean playerNear = check.doCheck(Check.CheckMode.PROXIMITY);

        // Decide emotion.
        feel.setParameters(lowHealth, playerNear);
        feel.doFinal();

        // Decide how to behave...
        if (curEmotion == EmotionalState.INTIMIDATED){
            // IMPLEMENT THE CHECK.
            boolean retaliationViable = check.doCheck(Check.CheckMode.COUNTER_ATTACK_VIABLE);
            if (retaliationViable) {
                // IMPLEMENT THE BEHAVIOUR.
                behaviours.getBehaviour("CounterAttack").run();
            }
            behaviours.getBehaviour("Travel").doAction();
         }

        else if (curEmotion == EmotionalState.AGGRESSIVE) {

            // Give the AI a 50% chance of rethinking strategy after at least 2 seconds.
            if ((tickCount/60) >= 2) {
                if (gen.nextDouble() >= 0.5) {
                    behaviours.getBehaviour("Strategise").run();
                    tickCount = 0;
                }
            }
            // Execute strategy.
            tickCount++;
            if (!currentStrategy.isRunning()) {
                currentStrategy.start();
            } else {
                currentStrategy.doAction();
            }
        }

        else if (curEmotion == EmotionalState.BORED) {
            hunt.doAction();
        }

    }

    protected void handleEmotion() {
        if (curEmotion == EmotionalState.INTIMIDATED) {
            this.stress = 80;
            flee.run();
        }
        else if (curEmotion == EmotionalState.AGGRESSIVE) {
            this.stress = 50;
            behaviours.getBehaviour("Strategise").run();
        }
        else if (curEmotion == EmotionalState.BORED) {
            this.stress = 0;
            hunt.start();
        }
    }

    public int getStressLevel(){
        return stress;
    }

    public void setStrategy(String strategy) {
        this.currentStrategy = (Sequence) behaviours.getBehaviour(strategy);
    }
}
