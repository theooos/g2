package server.ai.decision;

import server.ai.Intel;
import server.game.AIPlayer;
import server.game.Map;
import server.game.Orb;
import server.game.Player;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rhys on 3/11/17.
 */
public class PlayerIntel extends Intel {

    private boolean phaseShiftFailed;

    public PlayerIntel(ConcurrentHashMap<Integer, Player> players, Map map){
        super(players, map);
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
}
