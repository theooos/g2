package server.game;

import server.ai.OrbBrain;
import server.ai.Intel;
import server.ai.vision.VisibilityPolygon;

import java.util.HashMap;

/**
 * Represents an artificially intelligent entity within the game that can see
 * and attack players in its observable surroundings, irrespective of which
 * Phase the players are in. Orbs are naturally passive and will attempt to escape
 * when attacked (shrinking themselves for protection), but pack a very powerful
 * electrical attack when enemy players find themselves within touching range.
 * Created by Peran and Rhys on 01/02/17.
 */
public class Orb extends MovableEntity {

    transient private Intel intel;
    transient private OrbBrain myBrain;

    /**
     * Creates an Orb.
     * @param pos - The Orb's starting position.
     * @param dir - The Orb's starting direction.
     * @param team - The team the Orb is a part of.
     * @param phase - The phase the Orb exists within.
     */
    public Orb(Vector2 pos, Vector2 dir, int team, int phase, int id) {

        // Initialise "Movable Entity" members.
        this.speed = 1;
        this.dir = dir;
        this.radius = 10;
        this.team = team;

        // Initialise "Entity" members.
        this.damageable = true;
        this.pos = pos;
        this.maxHealth = 50;
        this.health = maxHealth;
        this.phase = phase;
        this.visible = true;
        this.ID = id;
    }

    /**
     * Calls upon this Orb's brain to make this Orb perform an appropriate action.
     */
    public void live() {
        myBrain.doSomething();
    }


    public VisibilityPolygon getSight(){
        return intel.getSight();
    }

    /**
     * Re-initialises this Orb's Intel and Brain. To be called at the beginning of each new game
     * with an appropriate (new) Intel object.
     * @param intel - the Intel containing details about the next game.
     */
    public void prepareOrbForGame(Intel intel, HashMap<Integer, Orb> orbs){
        this.intel = intel;
        this.intel.assignEntity(this);
        this.intel.setOrbs(orbs);
        this.myBrain = new OrbBrain(intel);
    }
}
