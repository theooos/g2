package server.ai.decision;

import server.ai.AIBrain;
import server.ai.behaviour.*;

/**
 * Represents the brain of an Orb, making decisions on the Orb's behalf while taking
 * the Orb's situation and surroundings into account.
 * Created by Rhys on 2/20/17.
 */
public class OrbBrain extends AIBrain {

    private OrbIntel intel;
    private Sequence drift;     // Allows the Orb to drift aimlessly around the map when it is BORED.
    private boolean playerNear;

    /**
     * Constructs an Orb's Brain - the decision maker of an Orb.
     * @param intel - The Intel object the brain utilises to make decisions.
     */
    public OrbBrain(OrbIntel intel) {
        super(intel);
        this.intel = intel;
        this.playerNear = false;
    }

    protected void constructBehaviours(){
        super.constructBehaviours();
        behaviours.addBehaviour(new Glide(intel, this), "Glide");
    }

    /**
     * Arranges the available behaviours into sequences that the Orb
     * will carry out under specific circumstances.
     */
    protected void configureBehaviours(){
        this.drift = new Sequence(intel, this);
        this.drift.add(behaviours.getBehaviour("Dawdle"));
        this.drift.add(behaviours.getBehaviour("Wander"));
        this.drift.add(behaviours.getBehaviour("FindPath"));
        this.drift.add(behaviours.getBehaviour("Glide"));
    }

    /**
     * Causes the Orb to carry out the most appropriate task, by evaluating
     * its current situation and surrounding environment.
     */
    public void doSomething(){

        check();

        if (!intel.ent().isAlive()){
            return;
        }

        // Decide what to do.
        if (!playerNear) {
            drift.doAction();
        }
        else {

            // Compute/relevantEnt-compute travel path if the target has moved since the last tick.
            if (check.doCheck(Check.CheckMode.TARGET_MOVED)) {
                intel.setTargetLocation(intel.getRelevantEntity().getPos());
                behaviours.getBehaviour("FindPath").run();
                behaviours.getBehaviour("Glide").start();
            } // Or if it hasn't...
            else {
                // Float towards the target player if they'relevantEnt out of attacking range.
                if (!check.doCheck(Check.CheckMode.RANGE)) {
                    behaviours.getBehaviour("Glide").doAction();
                }
                // Or, if the target is in range, zap them.
                else {
                    behaviours.getBehaviour("Zap").run();
                }
            }
        }
    }

    /**
     * Determines how the Orb behaves when it experiences a change in circumstances.
     */
    private void check() {

        boolean tempPN = check.doCheck(Check.CheckMode.PROXIMITY);

        if (tempPN != playerNear){
            playerNear = tempPN;
            behaviours.resetAll();

            if (!playerNear) {
                intel.ent().setSpeed(0.5F);
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(false);
                drift.start();
            } else {
                intel.ent().setSpeed(1F);
                ((FindPath)behaviours.getBehaviour("FindPath")).setSimplePath(true);
                behaviours.getBehaviour("Zap").start();
            }
        }
    }

}
