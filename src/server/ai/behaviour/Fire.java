package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * Created by rhys on 3/11/17.
 */
public class Fire extends PlayerTask {

    public Fire(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
    }

    public boolean checkConditions(){
        return (intel.getRelevantEntity()!= null);
    }

    public void doAction(){

        // Set the player to (roughly) face the player.
        Vector2 dir = intel.ent().getPos().vectorTowards(intel.getRelevantEntity().getPos());
        dir = Vector2.deviate(dir, 2);
        intel.ent().setDir(dir);

        // Fire!
        intel.ent().setFiring(true);
    }
}
