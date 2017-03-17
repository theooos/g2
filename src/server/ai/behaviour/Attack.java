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

    private Player me;
    private Player target;
    private float maxRange;
    private float minRange;

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
        this.minRange = minRange;
        ((Fire)brain.getBehaviour("Fire")).setParameters(fireFreq);
    }

    public void start(){
        super.start();

        this.me = intel.ent();
        this.target = (Player) intel.getRelevantEntity();

        intel.setTargetLocation(target.getPos());
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(true);
        brain.getBehaviour("FindPath").run();

        brain.getBehaviour("Fire").start();
    }

    public void doAction(){

        // Recompute travel path if necessary.
        if (brain.performCheck(Check.CheckMode.TARGET_MOVED)) {
            System.out.println("Target moved: Recomputing path.");
            brain.getBehaviour("FindPath").run();
        }

        // Check if the target is in firing range.
        float distance = me.getPos().getDistanceTo(target.getPos());
        if (distance < maxRange) {
            System.out.println("Target in range.");
            brain.getBehaviour("Fire").doAction();

            if (distance > minRange) {
                System.out.println("Could be closer.");
                brain.getBehaviour("Travel").doAction();
            }
        }
        else {
            System.out.println("Getting into range.");
            me.setFiring(false);
            brain.getBehaviour("Travel").doAction();
        }

        if (!target.isAlive()){
            me.setFiring(false);
            System.out.println("Mission accomplished.");
            end();
        }
    }
}
