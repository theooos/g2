package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/20/17.
 * Used to test the collision manager
 */
class CollisionManagerTest {
    private CollisionManager collisionManager;
    private Map map;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private HashMap<Integer, PowerUp> powerUps;

    @BeforeEach
    void setUp() {
        try {
            map = new Map(10);
            players = new ConcurrentHashMap<>();
            orbs = new HashMap<>();
            powerUps = new HashMap<>();
            collisionManager = new CollisionManager(players, orbs, map, powerUps);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void collidesWithPowerUp() {
        Player p = new Player(new Vector2(30, 30), null, 0, 0, null, null, 0);
        powerUps.put(1, new PowerUp(new Vector2(30, 30), PowerUp.Type.heat, 1, 0));
        collisionManager = new CollisionManager(players, orbs, map, powerUps);
        assertTrue(collisionManager.collidesWithPowerUp(p) != null);
        p.setPos(new Vector2(p.getPos().getX()+p.getRadius()+100, p.getPos().getY()+p.getRadius()+100));
        assertTrue(collisionManager.collidesWithPowerUp(p) == null);
        p.setPos(new Vector2(30+p.getRadius()-1, 30+p.getRadius()-1));
        assertTrue(collisionManager.collidesWithPowerUp(p) != null);
    }

    @Test
    void validPosition() {

    }

    @Test
    void validPosition1() {

    }

    @Test
    void orbValidPosition() {

    }

    @Test
    void pointWallCollision() {

    }

    @Test
    void collidesWithPlayerOrBot() {
        setUpBotsAndPlayer();

        Player p = new Player(new Vector2(100, 100), new Vector2(1, 0), 0, 0, null, null, 2);
        assertTrue(collisionManager.collidesWithPlayerOrBot(p) == null);

        p = new Player(new Vector2(100, 100), new Vector2(1, 0), 0, 0, null, null, 3);
        assertTrue(collisionManager.collidesWithPlayerOrBot(p) != null);

        p = new Player(new Vector2(10, 10), new Vector2(1, 0), 0, 1, null, null, 3);
        assertTrue(collisionManager.collidesWithPlayerOrBot(p) != null);

        p = new Player(new Vector2(10, 10), new Vector2(1, 0), 0, 0, null, null, 3);
        assertTrue(collisionManager.collidesWithPlayerOrBot(p) == null);

        p = new Player(new Vector2(400, 400), new Vector2(1, 0), 0, 0, null, null, 3);
        assertTrue(collisionManager.collidesWithPlayerOrBot(p) == null);

    }

    @Test
    void collidesWithPlayerOrBot1() {
        setUpBotsAndPlayer();
        assertTrue(collisionManager.collidesWithPlayerOrBot(20, new Vector2(10, 10)) != null);
        assertTrue(collisionManager.collidesWithPlayerOrBot(20, new Vector2(50, 20)) != null);
        assertTrue(collisionManager.collidesWithPlayerOrBot(20, new Vector2(100, 130)) != null);
        assertTrue(collisionManager.collidesWithPlayerOrBot(20, new Vector2(200, 20)) == null);
    }

    private void setUpBotsAndPlayer() {
        Orb o = new Orb(new Vector2(10, 10), null, 1, 0);
        orbs.put(0, o);
        o = new Orb(new Vector2(40, 40), null, 1, 1);
        orbs.put(1, o);
        Player p = new Player(new Vector2(100, 100), null, 0, 0, null, null, 2);
        players.put(2, p);
        p = new Player(new Vector2(100, 100), null, 0, 1, null, null, 3);
        players.put(3, p);
        
    }

    @Test
    void projectileWallCollision() {
        assertTrue(collisionManager.projectileWallCollision(new Vector2(150, 299), new Vector2(0, 1), 10, 0));
        assertFalse(collisionManager.projectileWallCollision(new Vector2(150, 299), new Vector2(0, 1), 10, 1));

        assertFalse(collisionManager.projectileWallCollision(new Vector2(150, 299), new Vector2(1, 0), 10, 0));
        assertTrue(collisionManager.projectileWallCollision(new Vector2(150, 299), new Vector2(1, 1).normalise(), 10, 0));

        assertFalse(collisionManager.projectileWallCollision(new Vector2(150, 280), new Vector2(0, 1), 10, 0));
    }

}