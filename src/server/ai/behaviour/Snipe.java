package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * Created by rhys on 3/11/17.
 */
public class Snipe extends PlayerTask {

    private int timer;

    public Snipe(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
        this.timer = 0;
    }

    public boolean checkConditions(){
        return (intel.getTargetPlayer()!= null);
    }

    public void doAction(){
        timer++;

        // Every 5 ticks:
        if (timer%5 == 0) {
            Vector2 dir = intel.ent().getPos().vectorTowards(intel.getTargetPlayer().getPos());
            dir = Vector2.deviate(dir, brain.getStressLevel());
            intel.ent().setDir(dir);
        }

        // Every 30 ticks (half a second):
        if (timer == 30) {
            intel.ent().setFiring(true);
        }

        // After every 30th tick.
        if (timer > 30) {
            intel.ent().setFiring(false);
            timer = 0;
        }
    }
}
