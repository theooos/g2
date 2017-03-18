package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Player;
import server.game.Vector2;

/**
 * Allows the player to attack an opponent player within line of sight.
 * Created by rhys on 3/11/17.
 */
public class Attack extends PlayerTask {

    private Player me;
    private Player target;
    private float maxRange;
    private float targetRange;

    public Attack(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    public boolean checkConditions(){
        return intel.getRelevantEntity() != null &&
                intel.getRelevantEntity() instanceof Player &&
                intel.getRelevantEntity().isAlive();
    }

    public void setParameters(float maxRange, float minRange, int fireFreq){
        this.maxRange = maxRange;
        this.targetRange = minRange;
        ((Fire)brain.getBehaviour("Fire")).setParameters(fireFreq);
    }

    public void start(){
        super.start();

        this.me = intel.ent();
        this.target = (Player) intel.getRelevantEntity();

        track();
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(true);
        brain.getBehaviour("FindPath").run();

        brain.getBehaviour("Fire").start();
    }

    public void doAction(){

        if (!target.isAlive()){
            me.setFiring(false);
            end();
            return;
        }

        // Recompute travel path if necessary.
        if (brain.performCheck(Check.CheckMode.TARGET_MOVED)) {
            track();
            brain.getBehaviour("FindPath").run();
        }

        float distance = me.getPos().getDistanceTo(target.getPos());

        // Move towards optimum distance from target.
        if (distance > (targetRange + 2) || distance < (targetRange - 2)) {
            Vector2 dir = me.getDir();
            brain.getBehaviour("Travel").doAction();
            me.setDir(dir);
        }

        // Check if the target is in firing range.
        if (distance < maxRange) {
            brain.getBehaviour("Fire").doAction();
        }
        else {
            me.setFiring(false);
        }
    }

    private void track(){
        Vector2 fullVector = me.getPos().vectorTowards(target.getPos());
        float distanceToTravel = me.getPos().getDistanceTo(target.getPos()) - targetRange;
        intel.setTargetLocation(fullVector.clampedTo(distanceToTravel).add(me.getPos()));
    }
}