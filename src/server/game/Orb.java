package server.game;

import server.ai.behaviour.*;
import server.ai.Intel;

/**
 * Created by peran on 01/02/17.
 */
public class Orb extends MovableEntity {

    private Behaviour tree;
    private Intel intel;

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
        createBehaviourTree(intel);
    }

    /**
     * Makes this Orb behave in an appropriate manner when triggered by the Game Loop.
     */
    public void live() {
        this.tree.doAction();
    }

    public void start() {
        this.tree.getControl().safeStart();
    }

    private void createBehaviourTree(Intel intel) {

        // Feed this Orb into the environment intel object.
        intel.assignEntity(this);

        Behaviour live = new Sequence(intel, "Live");
        live = new ResetDecorator(intel, live);

        Behaviour feel = new Selector(intel, "Feel");

        // Create "Scared" branch -----------------------------------------------
        Behaviour scared = new Selector(intel, "Scared");
        scared = new ScaredDecorator(intel, scared);

        // Create "Flee" sequence -------------------------
        Behaviour flee = new Sequence(intel, "Flee");
        //(PlannerController)flee.getControl()).add(new LocateCover(intel, this));
        ((PlannerController)flee.getControl()).add(new Wander(intel));
        ((PlannerController)flee.getControl()).add(new FindPath(intel));
        ((PlannerController)flee.getControl()).add(new Travel(intel));

        ((PlannerController)scared.getControl()).add(new Travel(intel));
        ((PlannerController)scared.getControl()).add(flee);


        // Create "Angry" branch ------------------------------------------------
        Behaviour angry = new Selector(intel, "Angry");
        angry = new AngryDecorator(intel, angry);

        // Create "Attack" sequence -----------------------
        Behaviour attack = new Sequence(intel, "Attack");
        attack = new AttackDecorator(intel, attack);
        ((PlannerController)attack.getControl()).add(new Travel(intel));
        ((PlannerController)attack.getControl()).add(new Zap(intel));

        // Create "Hunt" sequence -------------------------
        Behaviour hunt = new Sequence(intel, "Hunt");
        ((PlannerController)hunt.getControl()).add(new AcquireTarget(intel));
        ((PlannerController)hunt.getControl()).add(new FindPath(intel));
        ((PlannerController)hunt.getControl()).add(new Travel(intel));
        ((PlannerController)hunt.getControl()).add(new Zap(intel));

        ((PlannerController)angry.getControl()).add(attack);
        ((PlannerController)angry.getControl()).add(hunt);


        // Create "Relaxed" branch ----------------------------------------------
        Selector relaxed = new Selector(intel, "Relaxed");

        // Create "Drift" sequence ------------------------
        Behaviour drift = new Sequence(intel, "Drift");
        ((PlannerController)drift.getControl()).add(new Wander(intel));
        ((PlannerController)drift.getControl()).add(new FindPath(intel));
        ((PlannerController)drift.getControl()).add(new Travel(intel));

        ((PlannerController)relaxed.getControl()).add(new Travel(intel));
        ((PlannerController)relaxed.getControl()).add(drift);


        // Final Construction.
        ((PlannerController)feel.getControl()).add(scared);
        ((PlannerController)feel.getControl()).add(angry);
        ((PlannerController)feel.getControl()).add(relaxed);


        ((PlannerController)live.getControl()).add(new HealthCheck(intel));
        ((PlannerController)live.getControl()).add(new LookAround(intel));
        ((PlannerController)live.getControl()).add(feel);

        this.tree = live;
    }
}
