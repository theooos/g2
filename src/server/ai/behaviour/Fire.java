package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.AIConstants;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Vector2;

/**
 * This behaviour allows an AI-controlled player to fire their weapon at
 * a suitable frequency.
 *
 * Created by Rhys on 3/11/17.
 */
public class Fire extends PlayerTask {

    private int fireFreq;
    private int fireDelay;

    /**
     * Constructs a Fire behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
    public Fire(PlayerIntel intel, PlayerBrain brain) {
        super(intel, brain);
    }

    @Override
    public boolean checkConditions(){
        return (intel.getRelevantEntity()!= null);
    }

    /**
     * Sets the appropriate firing frequency for the currently equipped weapon.
     *
     * @param fireFreq delay between shots (in number of ticks).
     */
    public void setParameters(int fireFreq){
        this.fireFreq = fireFreq;
    }

    @Override
    public void start(){
        super.start();
        fireDelay = 0;
    }

    @Override
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
