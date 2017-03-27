package server.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.ai.decision.OrbIntel;
import server.game.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by rhys on 3/27/17.
 */
class IntelTest {

    private Intel intel;

    @BeforeEach
    void setUp() {
        ConcurrentHashMap<Integer, Player> players = new ConcurrentHashMap<>();
        players.put(0, new Player(new Vector2(50, 50), null, 1, 1, null, null, 0));

        Map map = new Map(1);

        HashMap<Integer, PowerUp> pUps = new HashMap<>();
        pUps.put(0, new PowerUp(new Vector2(0, 1), PowerUp.Type.health, 0, 1));

        this.intel = new OrbIntel(players, map, pUps);
    }

    void initIntel(){
        Orb orb = new Orb(null, null, 0, 0);
        HashMap<Integer, Orb> orbs = new HashMap<>();
        orbs.put(0, orb);

        this.intel.initForGame(orb, orbs);
    }

    @Test
    void ent() {
        Orb orb = new Orb(null, null, 0, 0);
        HashMap<Integer, Orb> orbs = new HashMap<>();
        orbs.put(0, orb);

        this.intel.initForGame(orb, orbs);

        MovableEntity testEnt = intel.ent();

        assertSame(orb, testEnt);
        assertEquals(orb.getID(), testEnt.getID());
    }

    @Test
    void getPlayers() {
        ConcurrentHashMap<Integer, Player> testPlayers = intel.getPlayers();
        assertNotNull(testPlayers);
        assertTrue(testPlayers.size() == 1);
    }

    @Test
    void getPlayer() {
        int testID = 0;
        Player testPlayer = intel.getPlayer(testID);
        assertEquals(testID, testPlayer.getID());
    }

    @Test
    void getOrbs() {

        HashMap<Integer, Orb> orbs = intel.getOrbs();
        assertNull(orbs);

        initIntel();
        orbs = intel.getOrbs();
        assertNotNull(orbs);
        assertTrue(orbs.size() == 1);
    }

    @Test
    void getMap() {
        Map map = intel.getMap();
        assertNotNull(map);
        assertEquals(1, map.getID());
    }

    @Test
    void setMap() {
        Map map = new Map(2);
        assertNotNull(map);

        intel.setMap(map);

        assertSame(map, intel.getMap());
    }

    @Test
    void getTargetLocation() {
        assertNull(intel.getTargetLocation());

        Vector2 targetPos = new Vector2(30, 30);
        intel.setTargetLocation(targetPos);
        assertNotNull(intel.getTargetLocation());
        assertSame(targetPos, intel.getTargetLocation());
    }
}