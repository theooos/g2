package server.game;

import server.ai.behaviour.*;
import server.ai.Intel;
import server.ai.behaviour.FindPath;
import server.ai.behaviour.PlannerController;
import server.ai.behaviour.decorator.*;
import server.ai.behaviour.Travel;
import server.ai.behaviour.Wander;
import server.ai.behaviour.Zap;

/**
 * Created by peran on 01/02/17.
 */
public class Orb extends MovableEntity implements Runnable {

    private Intel intel;
    Thread thread;

    /**
     * The basic AI controlled enemy
     * @param pos starting pos
     * @param dir starting dir
     * @param team the team the is on
     * @param phase starting phase
     */
    public Orb(Vector2 pos, Vector2 dir, int team, int phase, int id, Intel intel) {
        this.pos = pos;
        this.dir = dir;
        this.team = team;
        this.phase = phase;
        this.damageable = true;
        this.visible = true;
        maxHealth = 50;
        this.health = maxHealth;
        this.speed = 2;
        this.team = team;
        radius = 10;
        ID = id;
        this.intel = intel;
        createBehaviourTrees(intel);
        thread = new Thread(this, "Orb01");
        thread.start();
    }


    /**
     * Makes this Orb behave in an appropriate manner when triggered by the Game Loop.
     */
    public void live() {
        feel();
        if (emotion == emotionalState.SCARED){
            if (!scared.getControl().started()){
                scared.getControl().safeStart();
            } else {
                scared.doAction();
            }
        } else if (emotion == emotionalState.ANGRY){
            if (!angry.getControl().started()){
                angry.getControl().safeStart();
            } else {
                angry.doAction();
            }
        } else {
            if (!relaxed.getControl().started()) {
                relaxed.getControl().safeStart();
            } else {
                relaxed.doAction();
            }
        }
    }


    /**
     * Determines how this Orb is feeling.
     */
    private void feel(){
        int currentHealth = intel.ent().getHealth();

        // If the entity has lost health since the last tick, be scared.
        if (currentHealth < intel.healthLastTick()){
            intel.emotionalStateChanged(emotion != emotionalState.SCARED);
            this.emotion = emotionalState.SCARED;
        }
        // If there is an enemy nearby, be angry.
        else if (playerNearby()) {
            intel.emotionalStateChanged(emotion != emotionalState.ANGRY);
            this.emotion = emotionalState.ANGRY;
            locateTarget();
        }
        // Otherwise, just chill.
        else {
            intel.emotionalStateChanged(emotion != emotionalState.RELAXED);
            this.emotion = emotionalState.RELAXED;
        }
        intel.rememberHealth(currentHealth);
    }

    /**
     * Construct behaviour trees for each of this Orb's three possible emotional states.
     * @param intel - The object containing information the orb needs to take into account when acting.
     */
    private void createBehaviourTrees(Intel intel) {

        // Feed this Orb into the environment intel object.
        intel.assignEntity(this);

        // Create "Scared" tree -----------------------------------------------
        this.scared = new Sequence(intel, "Flee");
        scared = new ResetDecorator(intel, scared);
        //(PlannerController)flee.getControl()).add(new LocateCover(intel, this));
        ((PlannerController)scared.getControl()).add(new Wander(intel));
        ((PlannerController)scared.getControl()).add(new FindPath(intel));
        ((PlannerController)scared.getControl()).add(new Travel(intel));

        // Create "Angry" tree ------------------------------------------------
        this.angry = new Selector(intel, "Range Check");
        angry = new ResetDecorator(intel, angry);

        Behaviour movementCheck = new Selector(intel, "Movement Check");
        Behaviour hunt = new Travel(intel);
        hunt = new HuntDecorator(intel, hunt);

        Behaviour track = new Sequence(intel, "Track");
        ((PlannerController)track.getControl()).add(new FindPath(intel));
        ((PlannerController)track.getControl()).add(new Travel(intel));

        ((PlannerController)movementCheck.getControl()).add(hunt);
        ((PlannerController)movementCheck.getControl()).add(track);
        ((PlannerController)angry.getControl()).add(new Zap(intel));
        ((PlannerController)angry.getControl()).add(movementCheck);

        // Create "Relaxed" tree ----------------------------------------------
        this.relaxed = new Sequence(intel, "Drift");
        relaxed = new ResetDecorator(intel, relaxed);
        ((PlannerController)relaxed.getControl()).add(new Wander(intel));
        ((PlannerController)relaxed.getControl()).add(new FindPath(intel));
        ((PlannerController)relaxed.getControl()).add(new Travel(intel));
    }

    /**
     * @return true if there is a player in the entity's field of vision.
     */
    private boolean playerNearby(){
        // Code HERE for checking for nearby players.
        // PERQUISITE: Collision Detection.
        return true;
    }

    /**
     * Finds the current location of the target and stores it in this Orb's Intel.
     */
    private void locateTarget(){
        Player target = intel.getPlayer(0);
        intel.setTargetPlayer(target);
        intel.setTargetLocation(target.getPos());
    }

    @Override
    public void run() {
        while(true) {
            live();
            try {
                thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
