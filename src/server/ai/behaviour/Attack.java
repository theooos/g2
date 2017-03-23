package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Orb;
import server.game.Player;
import server.game.Vector2;

import static sun.audio.AudioPlayer.player;

/**
 * Allows AI-controlled players to attack an opponent player within line of sight,
 * adhering to a pre-determined strategy.
 * 
 * Created by rhys on 3/11/17.
 */
public class Attack extends PlayerTask {

    private Player me;
    private Player target;
    private float maxRange;
    private float targetRange;

    /**
     * Constructs an Attack behaviour object that utilises the given Intel and Brain objects.
     *
     * @param intel the game-related intelligence the behaviour uses to make decisions and
     *              carry out actions.
     * @param brain the brain of the AI player that will be exhibiting this behaviour.
     */
    public Attack(PlayerIntel intel, PlayerBrain brain){
        super(intel, brain);
    }

    @Override
    public boolean checkConditions(){
        return intel.getRelevantEntity() != null &&
                intel.getRelevantEntity() instanceof Player &&
                intel.getRelevantEntity().isAlive();
    }

    /**
     * Sets the parameters that this attack object needs to be able to function.
     *
     * @param maxRange    the maximum range from which the weapon can be fired and
     *                    still do damage to the target.
     * @param targetRange the distance from the target that the current strategy
     *                    calls for as the optimum attacking range.
     * @param fireFreq    an appropriate firing frequency for the weapon.
     */
    public void setParameters(float maxRange, float targetRange, int fireFreq){
        this.maxRange = maxRange;
        this.targetRange = targetRange;
        ((Fire)brain.getBehaviour("Fire")).setParameters(fireFreq);
    }

    @Override
    public void start(){
        super.start();

        this.me = intel.ent();
        this.target = (Player) intel.getRelevantEntity();

        track();
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(true);
        brain.getBehaviour("FindPath").run();

        brain.getBehaviour("Fire").start();
    }

    @Override
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
            brain.getBehaviour("Travel").doAction();
        }

        // Check if the target is in firing range.
        if (distance < maxRange) {
            brain.getBehaviour("Fire").doAction();
        }
        else {
            me.setFiring(false);
        }
    }

    /**
     * Calculates the location the AI should be travel towards in order to be in
     * optimum attacking range of the target.
     */
    private void track(){
        Vector2 fullVector = me.getPos().vectorTowards(target.getPos());
        float distanceToTravel = me.getPos().getDistanceTo(target.getPos()) - targetRange;
        intel.setTargetLocation(fullVector.clampedTo(distanceToTravel).add(me.getPos()));
    }
}
