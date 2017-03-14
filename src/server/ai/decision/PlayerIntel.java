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

    private boolean phaseShiftAttempted;
    private int originPhase;

    public PlayerIntel(ConcurrentHashMap<Integer, Player> players, Map map){
        super(players, map);
        phaseShiftAttempted = false;
        originPhase = 0;
    }

    public AIPlayer ent(){
        return (AIPlayer) ent;
    }

    @Override
    protected void constructVisualiser() {
        this.sight = new Visualiser(map, players, allOrbs, ent.getID());
    }

    public boolean validPosition(){
        return collisionManager.validPosition(ent);
    }

    public Visualiser getVisualiser(){
        return sight;
    }

    public void attemptedPhaseShift(int fromPhase){
        this.phaseShiftAttempted = true;
        this.originPhase = fromPhase;
    }

    public boolean phaseShiftAttempted() {
        return phaseShiftAttempted;
    }

    public boolean phaseShiftSuccessful(){
        boolean r = phaseShiftAttempted;
        phaseShiftAttempted = false;
        return r && (ent.getPhase() != originPhase);
    }

    public ConcurrentHashMap<Integer, Orb> getOrbsInSight(){
        return sight.getOrbsInSight(ent.getPos().toPoint(), ent.getPhase(), 0);
    }
}
