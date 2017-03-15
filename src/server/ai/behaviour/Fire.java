package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * Created by rhys on 3/11/17.
 */
public class Fire extends PlayerTask {

    private int timer;

    public Fire(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
        this.timer = 0;
    }

    public boolean checkConditions(){
        return (intel.getRelevantEntity()!= null);
    }

    public void doAction(){
        timer++;

        Vector2 dir = intel.ent().getPos().vectorTowards(intel.getRelevantEntity().getPos());
        dir = Vector2.deviate(dir, 2);
        intel.ent().setDir(dir);

        // Fire as often as possible (give or take):
        int fireFreq = intel.ent().getActiveWeapon().getRefireTime() + 10;
        if (timer == fireFreq) {
            intel.ent().setFiring(true);
        }

        // After every 30th tick.
        if (timer > fireFreq) {
            intel.ent().setFiring(false);
            timer = 0;
        }
    }
}
