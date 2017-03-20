package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.AIConstants;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * Created by rhys on 3/11/17.
 */
public class Fire extends PlayerTask {

    private int fireFreq;
    private int fireDelay;

    public Fire(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
    }

    public boolean checkConditions(){
        return (intel.getRelevantEntity()!= null);
    }

    public void setParameters(int fireFreq){
        this.fireFreq = fireFreq;
    }

    @Override
    public void start(){
        super.start();
        fireDelay = 0;
    }

    public void doAction(){

        // Set the player to face the player.
        Vector2 dir = intel.ent().getPos().vectorTowards(intel.getRelevantEntity().getPos());
        intel.setLastCursorPos(intel.getRelevantEntity().getPos());

        //int inaccuracy = (int) Math.ceil(brain.getStressLevel() * AIConstants.MAX_AIM_INACCURACY);
        //dir = Vector2.deviate(dir, inaccuracy);

        // Fire - if the weapon will allow.
        if (fireDelay == 0 || intel.ent().getActiveWeapon().isFullyAuto()) {
            intel.ent().setDir(dir.normalise());
            intel.ent().setFiring(true);
        }

        else if ((fireDelay == 1) && !intel.ent().getActiveWeapon().isFullyAuto()) {
            intel.ent().setFiring(false);
            fireDelay = -fireFreq;
        }

        fireDelay++;
    }
}
