package server.ai.decision;

import server.ai.Intel;
import server.ai.vision.Visualiser;
import server.game.AIPlayer;
import server.game.Map;
import server.game.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rhys on 3/11/17.
 */
public class PlayerIntel extends Intel {

    private int healthLastTick;


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

    public int healthLastTick() {
        return healthLastTick;
    }

    public void rememberHealth(int healthLastTick) {
        this.healthLastTick = healthLastTick;
    }

    public boolean validPosition(){
        return collisionManager.validPosition(ent);
    }
}
