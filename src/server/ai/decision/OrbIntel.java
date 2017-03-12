package server.ai.decision;

import server.ai.Intel;
import server.ai.vision.Visualiser;
import server.game.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A packet of the necessary data that AI units need to make decisions.
 * Created by Rhys on 2/15/17.
 */
public class OrbIntel extends Intel {

    /**
     * Constructs an intel object based on the given Players and Map.
     * @param players - The list of players.
     * @param map - The map currently in play.
     */
    public OrbIntel(ConcurrentHashMap<Integer, Player> players, Map map) {
        super(players, map);
    }

    /**
     * @return the Orb that owns this intel object.
     */
    public Orb ent(){
        return (Orb) ent;
    }

    public boolean validPosition(){
        return collisionManager.orbValidPosition((Orb)ent);
    }

    @Override
    protected void constructVisualiser() {
        this.sight = new Visualiser(map, players, ent.getID());
    }

}
