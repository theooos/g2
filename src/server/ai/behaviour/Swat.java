package server.ai.behaviour;

import server.ai.PlayerTask;
import server.ai.decision.AIConstants;
import server.ai.decision.LoadoutHandler;
import server.ai.decision.PlayerBrain;
import server.ai.decision.PlayerIntel;
import server.game.Orb;
import server.game.Player;
import server.game.Vector2;

/**
 * Allows the player to attack an opponent player within line of sight, while retreating.
 * Created by Rhys on 3/11/17.
 */
public class Swat extends PlayerTask {

    private Player me;
    private Orb target;
    private LoadoutHandler loadout;
    private int fireFreq;
    private int fireDelay;

    public Swat(PlayerIntel intel, PlayerBrain brain, LoadoutHandler ldh){
        super(intel, brain);
        this.loadout = ldh;
        fireDelay = 0;
    }

    public boolean checkConditions(){
        return intel.getRelevantEntity() != null &&
                intel.getRelevantEntity() instanceof Orb &&
                intel.getRelevantEntity().isAlive();
    }

    @Override
    public void start(){
        super.start();
        this.me = intel.ent();
        this.target = (Orb) intel.getRelevantEntity();

        // Is the Orb in this phase? If not, shift phase.
        if (intel.orbInOtherPhase()){
            brain.getBehaviour("ForceShiftPhase").run();
        }

        // Does the player have a shotgun?
        if (loadout.haveShotgun()) {
            loadout.equipShotgun();
        } else {
            loadout.equipSMG();
        }

        fireFreq = intel.ent().getActiveWeapon().getRefireTime();
        fireDelay = 0;
    }

    public void doAction(){

        // Take aim.
        me.setDir(me.getPos().vectorTowards(target.getPos()).normalise());
        int inaccuracy = (int) Math.ceil(brain.getStressLevel()* AIConstants.MAX_AIM_INACCURACY);
        Vector2.deviate(me.getDir(), inaccuracy);

        // If the time is right, fire.
        if ((fireDelay++ == 0) || me.getActiveWeapon().isFullyAuto()){
            brain.getBehaviour("Fire").run();
            fireDelay = -fireFreq;
        }

        else if (!me.getActiveWeapon().isFullyAuto()){
            me.setFiring(false);
        }

        // Check if the orb is dead.
        if (!target.isAlive()){
            end();
        }
    }

    @Override
    public void end(){

        // Return to original phase if required.
        if (intel.orbInOtherPhase()){
            brain.getBehaviour("ForceShiftPhase").run();
        }
        intel.setPhaseShiftReq(false);
    }
}
