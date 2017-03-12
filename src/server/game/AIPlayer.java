package server.game;

import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.ai.vision.VisibilityPolygon;

import java.util.HashMap;

/**
 * Created by peran on 01/02/17.
 */
public class AIPlayer extends Player {

    transient private PlayerIntel intel;
    transient private PlayerBrain myBrain;

    public AIPlayer(Vector2 pos, Vector2 dir, int team, int phase, Weapon w1, Weapon w2, int id) {
        super(pos, dir, team, phase, w1, w2, id);
    }

    public void live() {
        myBrain.doSomething();
    }

    public VisibilityPolygon getSight() {
        return intel.getSight();
    }

    public void preparePlayerForGame(PlayerIntel intel, HashMap<Integer, Orb> orbs){
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
