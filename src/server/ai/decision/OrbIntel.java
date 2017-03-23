package server.ai.decision;

import server.ai.Intel;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A packet of the necessary data that Orbs need to make decisions.
 *
 * Created by Rhys on 2/15/17.
 */
public class OrbIntel extends Intel {

    /**
     * Constructs an intelligence object based on the given Players and Map.
     *
     * @param players the list of players.
     * @param map     the map currently in play.
     */
    public OrbIntel(ConcurrentHashMap<Integer, Player> players, Map map, HashMap<Integer, PowerUp> pUps) {
        super(players, map, pUps);
    }

    /**
     * @return the intelligence object's parent orb.
     */
    public Orb ent(){
        return (Orb) ent;
    }

    /**
     * Checks whether or not the Orb's current position on the map is valid, using
     * a variation on the game's collision code that allows Orbs to be in the same space
     * as a player (because their attack requires touching range).
     *
     * @return <CODE>true</CODE> if the Orb's current position is valid.
     */
    public boolean validPosition(){
        return collisionManager.orbValidPosition((Orb)ent);
    }

}
