package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Player;

/**
 * Allows the player to attack an opponent player within line of sight.
 * Created by rhys on 3/11/17.
 */
public class Attack extends PlayerTask {

    private int tickCount;
    private Player me;
    private Player target;
    private float maxRange;
    private float minRange;
    private int fireFreq;

    public Attack(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
        tickCount = 0;
    }

    public boolean checkConditions(){
        return intel.getRelevantEntity() != null &&
                intel.getRelevantEntity() instanceof Player &&
                intel.getRelevantEntity().isAlive();
    }

    public void setParameters(float maxRange, float minRange, int fireFreq){
        this.maxRange = maxRange;
        this.minRange = minRange;
        this.fireFreq = fireFreq;
    }

    public void start(){
        super.start();
        this.me = intel.ent();
        this.target = (Player) intel.getRelevantEntity();
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(true);
        brain.getBehaviour("FindPath").run();
        tickCount = 0;
    }

    public void doAction(){

        // Recompute travel path if necessary.
        if (brain.performCheck(Check.CheckMode.TARGET_MOVED)) {
            brain.getBehaviour("FindPath").run();
        }

        // Check if the target is in firing range.
        float distance = me.getPos().getDistanceTo(target.getPos());
        if (distance < maxRange) {

            // If they are and the time is right, fire.
            if ((tickCount == fireFreq) || me.getActiveWeapon().isFullyAuto()){
                brain.getBehaviour("Fire").run();
            }
            // In the next tick, if not firing an SMG, stop firing.
            else if (tickCount > fireFreq && !me.getActiveWeapon().isFullyAuto()){
                me.setFiring(false);
                tickCount = 0;
            }

            if (distance > minRange) {
                brain.getBehaviour("Travel").doAction();
            }
        }
        else {
            brain.getBehaviour("Travel").doAction();
        }

        if (!target.isAlive()){
            end();
        }
    }
}
