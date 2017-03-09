package server.ai;

import server.ai.behaviour.Sequence;

/**
 * Created by rhys on 3/8/17.
 */
public class PlayerBrain extends AIBrain {

    private Sequence flee;
    private Sequence rage;
    private Sequence attack;
    private Sequence chase;
    private Sequence hunt;

    public PlayerBrain(Intel intel) {
        super(intel);
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

        // Decide how to behave.
        if (curEmotion == EmotionalState.INTIMIDATED){
            if (!flee.hasFinished()){
                flee.run();
            } else {
                // IMPLEMENT THE CHECK.
                boolean retaliationViable = check.doCheck(Check.CheckMode.COUNTER_ATTACK_VIABLE);
                if (retaliationViable) {
                    // IMPLEMENT THE BEHAVIOUR.
                    behaviours.getBehaviour("CounterAttack").run();
                }
                behaviours.getBehaviour("Travel").doAction();

            }
        }

    }

    protected void handleEmotion() {

    }
}
