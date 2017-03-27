package server.ai.decision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Rhys on 3/27/17.
 * Used to test Orb's intel
 */

class OrbIntelTest {

    private OrbIntel intel;

    @BeforeEach
    void setUp() {
        ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();
        players.put(0, new Player(new Vector2(50, 50), null, 1, 1, null, null, 0));

        Map map = new Map(1);

        HashMap<Integer, PowerUp> pUps = new HashMap<>();
        pUps.put(0, new PowerUp(new Vector2(0, 1), PowerUp.Type.health, 0, 1));

        this.intel = new OrbIntel(players, map, pUps);
    }

    @Test
    void ent() {

        Orb orb = new Orb(null, null, 0, 0);
        HashMap<Integer, Orb> orbs = new HashMap<>();
        orbs.put(0, orb);

        this.intel.initForGame(orb, orbs);

        Orb testOrb = intel.ent();

        assertSame(orb, testOrb);
        assertEquals(orb.getID(), testOrb.getID());
    }

    @Test
    void validPosition() {

        Orb orb = new Orb(null, null, 0, 0);
        HashMap<Integer, Orb> orbs = new HashMap<>();
        orbs.put(1, orb);

        this.intel.initForGame(orb, orbs);

        // Test valid position.
        intel.ent().setPos(new Vector2(20, 20));
        assertTrue(intel.validPosition());

        // Test invalid position (wall).
        intel.ent().setPos(new Vector2(0, 0));
        assertFalse(intel.validPosition());

        // Test player overlap.
        intel.ent().setPos(new Vector2(50, 50));
        assertTrue(intel.validPosition());
    }
}