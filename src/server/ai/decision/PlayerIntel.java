package server.ai.decision;

import server.ai.Intel;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rhys on 3/11/17.
 */
public class PlayerIntel extends Intel {

    private boolean escaped;
    private boolean phaseShiftFailed;
    private boolean orbInOtherPhase;
    private MovableEntity entityBuffer;

    public PlayerIntel(ConcurrentHashMap<Integer, Player> players, Map map, HashMap<Integer, PowerUp> pUps){
        super(players, map, pUps);
        this.phaseShiftFailed = false;
    }

    public AIPlayer ent(){
        return (AIPlayer) ent;
    }

    public boolean validPosition(){
        return collisionManager.validPosition(ent);
    }

    public Visualiser getVisualiser(){
        return sight;
    }

    public void failedPhaseShift(){
        this.phaseShiftFailed = true;
    }

    public boolean phaseShiftFailed(){
        boolean r = phaseShiftFailed;
        phaseShiftFailed = false;
        return r;
    }

    public ConcurrentHashMap<Integer, Orb> getOrbsInSight(){
        return sight.getOrbsInSight(ent.getPos().toPoint(), ent.getPhase(), 0);
    }

    public boolean orbInOtherPhase(){
        return orbInOtherPhase;
    }

    public void setPhaseShiftReq(boolean req){
        this.orbInOtherPhase = req;
    }

    public void setEscaped(boolean escaped){
        this.escaped = escaped;
    }

    public boolean haveEscaped(){
        return escaped;
    }

    public void resetIntel(){
        super.resetIntel();
        this.escaped = false;
        this.phaseShiftFailed = false;
        this.orbInOtherPhase = false;
    }

    public void setRelevantEntity(MovableEntity relEnt){
        this.entityBuffer = relEnt;
    }

    public void loadRelevantEntity(){
        this.relevantEnt = this.entityBuffer;
    }
}
