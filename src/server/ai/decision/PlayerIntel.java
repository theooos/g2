package server.ai.decision;

import server.ai.Intel;
import server.game.AIPlayer;
import server.game.Map;
import server.game.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rhys on 3/11/17.
 */
public class PlayerIntel extends Intel {

    public PlayerIntel(ConcurrentHashMap<Integer, Player> players, Map map){
        super(players, map);
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
}
