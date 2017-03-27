package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.game.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test the game data class
 */
class GameDataTest {
    private GameData gd;

    @BeforeEach
    void setUp() {
        LobbyData ld = new LobbyData(new InitPlayer[]{}, 10);
        InitGame ig = new InitGame(new HashMap<>(), new ConcurrentHashMap<>(), 10, new Scoreboard(3, 4), new HashMap<>(), ld);
        gd = new GameData(ig);
    }

    @Test
    void getPlayers() {
        assertTrue(gd.getPlayers().equals(new ConcurrentHashMap<>()));
    }

    @Test
    void getPlayer() {
        Player p = new Player(null, null, 0, 0, null, null, 0);
        gd.updatePlayer(p);
        assertTrue(gd.getPlayer(p.getID()).equals(p));
    }

    @Test
    void getProjectiles() {
        assertTrue(gd.getProjectiles().equals(new ConcurrentHashMap<>()));
    }

    @Test
    void updateProjectile() {
        Player player = new Player(null, null, 0, 0, null, null, 0);
        Projectile p = new Projectile(1, 1, 3, null, null, 1, 0, player, 1);
        gd.updateProjectile(p);
        ConcurrentHashMap<Integer, Projectile> ps = new ConcurrentHashMap<>();
        ps.put(p.getID(), p);
        assertTrue(gd.getProjectiles().equals(ps));
    }

    @Test
    void getOrbs() {
        assertTrue(gd.getOrbs().equals(new HashMap<>()));
    }

    @Test
    void getMapID() {
        assertTrue(gd.getMapID() == 10);
    }

    @Test
    void updateOrb() {
        Orb o = new Orb(null, null, 1, 1);
        gd.updateOrb(o);
        HashMap<Integer, Orb> os = new HashMap<>();
        os.put(o.getID(), o);
        assertTrue(gd.getOrbs().equals(os));
    }

    @Test
    void updatePlayer() {
        Player p = new Player(null, null, 0, 0, null, null, 0);
        gd.updatePlayer(p);
        assertTrue(gd.getPlayer(p.getID()).equals(p));
    }


    @Test
    void updateScoreboard() {
        Scoreboard sb = new Scoreboard(5, 6);
        gd.updateScoreboard(sb);
        assertTrue(gd.getScoreboard().equals(sb));
    }

    @Test
    void getScoreboard() {
        assertTrue(gd.getScoreboard().equals(new Scoreboard(3, 4)));
    }

    @Test
    void getPowerUps() {
        assertTrue(gd.getPowerUps().equals(new HashMap<>()));
    }

    @Test
    void updatePowerUp() {
        PowerUp p = new PowerUp(null, PowerUp.Type.heat, 1, 0);
        HashMap<Integer, PowerUp> ps = new HashMap<>();
        ps.put(p.getID(), p);
        gd.updatePowerUp(p);
        assertTrue(gd.getPowerUps().equals(ps));
    }

    @Test
    void getLobbyData() {
        LobbyData ld = new LobbyData(new InitPlayer[]{}, 10);
        assertTrue(gd.getLobbyData().equals(ld));
    }
}