package server.ai.decision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Rhys on 3/27/17.
 */

class OrbIntelTest {

    private Intel intel;

    @BeforeEach
    void setUp() {

        ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();
        players.put(0, new Player(new Vector2(1, 0), null, 1, 1, null, null, 0));

        Map map = new Map(0);

        HashMap<Integer, PowerUp> pUps = new HashMap<>();
        pUps.put(0, new PowerUp(new Vector2(0, 1), PowerUp.Type.health, 0, 1));

        this.intel = new OrbIntel(players, map, pUps);

        Orb orb = new Orb(null, null, 0, 0);
        HashMap<Integer, Orb> orbs = new HashMap<>();
        orbs.put(1, orb);

        this.intel.initForGame(orb, orbs);
    }

    @Test
    void ent() {




    }
}