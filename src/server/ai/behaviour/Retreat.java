package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.Check;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Player;
import server.game.Vector2;

/**
 * Allows the player to attack an opponent player within line of sight, while retreating.
 * Created by Rhys on 3/11/17.
 */
public class Retreat extends PlayerTask {

    private int tickCount;
    private Player me;
    private Player target;
    private float maxRange;
    private float minRange;
    private Vector2 retreatPos;
    private int fireFreq;

    public Retreat(PlayerIntel intel, PlayerBrain brain){
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

        // Calculate the position to retreat towards.
        this.retreatPos = (new Vector2(0, 0)).sub(me.getPos().vectorTowards(target.getPos()));
        retreatPos = retreatPos.clampedTo((int)((maxRange - minRange) - me.getPos().getDistanceTo(target.getPos())));
        retreatPos = me.getPos().add(retreatPos);

        // Set it as the destination.
        intel.setTargetLocation(retreatPos);
        ((FindPath)brain.getBehaviour("FindPath")).setSimplePath(true);
        brain.getBehaviour("FindPath").run();
        tickCount = 0;
    }

    public void doAction(){

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

            if (distance < minRange) {
                brain.getBehaviour("Travel").doAction();
                me.setDir(me.getPos().vectorTowards(target.getPos()));
            }
        }
        else {
            brain.getBehaviour("Travel").doAction();
            me.setDir(me.getPos().vectorTowards(target.getPos()));
        }

        if (!target.isAlive() || distance >= minRange){
            end();
        }
    }
}
