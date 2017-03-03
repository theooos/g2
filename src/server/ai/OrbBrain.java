package server.ai;

import server.ai.behaviour.*;

/**
 * Represents the brain of an Orb, making decisions on the Orb's behalf while taking
 * the Orb's situation and surroundings into account.
 * Created by Rhys on 2/20/17.
 */
public class OrbBrain {

    public enum EmotionalState{SCARED, ANGRY, RELAXED}  // Denote the various emotions the Orb can be experiencing.
    private EmotionalState curEmotion;                  // Stores the Orb's emotional state, which decides how it acts.

    private Intel intel;        // Stores information that the Orb needs for making decisions.
    private Check check;        // Allows the Orb to carry out a variety of checks pertaining to its surroundings.
    private Feel feel;          // Allows the Orb to determine its current emotional state.
    private Sequence flee;      // Allows the Orb to escape when it is SCARED.
    private Sequence drift;     // Allows the Orb to drift aimlessly around the map when it is RELAXED.
    private FindPath pathfinder;// Given a target location, determines a path for how to get there.
    private Travel traveller;   // Allows the Orb to travel along a predetermined path.
    private Zap zapper;         // Allows the Orb to damage an enemy player when in touching range.

    /**
     * Constructs an Orb's Brain - the decision maker of an Orb.
     * @param intel - The Intel object the brain utilises to make decisions.
     */
    public OrbBrain(Intel intel) {
        this.intel = intel;
        this.curEmotion = EmotionalState.RELAXED;
        constructBehaviours();
    }

    /**
     * Initialises all tasks and task-sequences that the Orb can carry out.
     */
    private void constructBehaviours(){

        this.feel = new Feel(intel, this);
        this.check = new Check(intel);

        this.drift = new Sequence(intel, this);
        this.drift.add(new Wander(intel, this));
        this.drift.add(new FindPath(intel, this));
        this.drift.add(new Travel(intel, this));

        this.flee = new Sequence(intel, this);
        this.flee.add(new LocateCover(intel, this));
        this.flee.add(new FindPath(intel, this));
        this.flee.add(new Travel(intel, this));

        this.pathfinder = new FindPath(intel, this);
        this.traveller = new Travel(intel, this);
        this.zapper = new Zap(intel, this);
    }

    /**
     * Causes the Orb to carry out the most appropriate task, by evaluating
     * its current situation and surrounding environment.
     */
    public void doSomething(){

        // Perform checks.
        boolean inPain = check.doCheck(Check.CheckMode.HEALTH);
        boolean playerNear = check.doCheck(Check.CheckMode.PROXIMITY);

        // Find emotion.
        feel.setParameters(inPain, playerNear);
        feel.run();

        // Decide what to do.
        if (curEmotion == EmotionalState.SCARED){
            System.out.println("Orb " + intel.ent().getID() + " is Scared.");
            flee.start();
        }
        else if (curEmotion == EmotionalState.RELAXED) {
            System.out.println("Orb " + intel.ent().getID() + " is Relaxed.");
            drift.start();
        }
        else if (curEmotion == EmotionalState.ANGRY) {

            System.out.println("Orb " + intel.ent().getID() + " is Angry.");

            // Compute/re-compute travel path if the target has moved since the last tick.
            if (check.doCheck(Check.CheckMode.TARGET_MOVED)) {
                intel.setTargetLocation(intel.getTargetPlayer().getPos());
                pathfinder.run();
                traveller.start();
            } // Or if it hasn't...
            else {
                // Travel towards the target player if they're out of attacking range.
                if (!check.doCheck(Check.CheckMode.RANGE)) {
                    traveller.doAction();
                }
                // Or, if the target is in range, zap them.
                else {
                    zapper.run();
                }
            }
        }
    }

    /**
     * @return the current emotional state of the Orb this Brain belongs to.
     */
    public EmotionalState getEmotion(){
        return curEmotion;
    }

    /**
     * Sets the Orb's emotional state for this tick and compares it with the emotional state
     * of the last tick, resetting progress in all task-sequences if the emotional state has changed.
     * @param newEmotion - the Orb's emotional state for this tick.
     */
    public void emotionTransition(EmotionalState newEmotion){
        if (newEmotion != curEmotion) {
            flee.reset();
            drift.reset();
            pathfinder.reset();
            zapper.reset();
            traveller.reset();

            curEmotion = newEmotion;
        }
    }
}
