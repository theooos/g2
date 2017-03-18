package server.game;

import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;

import java.util.HashMap;

/**
 * Represents an artificially intelligent entity that attempts to play the
 * game in a similar way to a human. AI Players share the same abilities as
 * human-controlled players: phase-shifting, weapon-firing and - because of
 * the top-down nature of the game - the ability to see enemy players that
 * aren't in direct line of sight.
 * Created by Rhys and Peran on 01/02/17.
 */
public class AIPlayer extends Player {

    transient private PlayerIntel intel;
    transient private PlayerBrain myBrain;

    AIPlayer(Vector2 pos, Vector2 dir, int team, int phase, Weapon w1, Weapon w2, int id) {
        super(pos, dir, team, phase, w1, w2, id);
        maxHealth = 100;
    }

    public void live() {
        getActiveWeapon().live();
        myBrain.doSomething();
    }

    void preparePlayerForGame(PlayerIntel intel, HashMap<Integer, Orb> orbs){
        this.intel = intel;
        this.intel.initForGame(this, orbs);
        this.myBrain = new PlayerBrain(intel);
        this.myBrain.equip();
    }

    public Weapon getWeapon1() {
        return this.w1;
    }

    public Weapon getWeapon2() {
        return this.w2;
    }

    public void toggleWeapon(){
        setWeaponOut(!w1Out);
    }

}
