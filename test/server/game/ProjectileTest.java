package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test projectiles and dist drop off projectiles
 */
class ProjectileTest {
    Projectile p;

    @BeforeEach
    void setUp() {
        p = new Projectile(1, 1, 20, new Vector2(1, 1), new Vector2(1, 0), 2, 1, null, 1);
    }

    @Test
    void live() {
        p.live();
        assertFalse(p.isAlive());
    }

    @Test
    void getDamage() {
        assertTrue(p.getDamage() == 1);
    }

    @Test
    void kill() {
        assertTrue(p.isAlive());
        p.kill();
        assertFalse(p.isAlive());
    }

    @Test
    void getPlayerID() {
        p.setPlayer(new Player(null, null, 1,1, null, null, 0));
        assertTrue(p.getPlayerID() == 0);
    }

    @Test
    void getPlayer() {
        p.setPlayer(new Player(null, null, 1,1, null, null, 0));
        assertTrue(p.getPlayer().equals(new Player(null, null, 1,1, null, null, 0)));
    }

    @Test
    void setPlayer() {
        p.setPlayer(new Player(null, null, 1,1, null, null, 1));
        assertTrue(p.getPlayerID() == 1);
    }

    @Test
    void setTeam() {
        p.setTeam(1);
        assertTrue(p.getTeam() == 1);
    }

    @Test
    void cloneTest() {
        Projectile p1 = p.clone();
        assertTrue(p1.equals(p));
    }

    @Test
    void distLive() {
        DistDropOffProjectile d = new DistDropOffProjectile(5, 5, 5, new Vector2(1, 1), new Vector2(1, 0), 0, 0, null, 1);
        d.live();
        assertTrue(d.getRadius() == 4);
        assertTrue(d.getDamage() == 4);
    }

}