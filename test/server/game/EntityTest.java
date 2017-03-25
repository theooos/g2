package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used
 */
class EntityTest {
    private Player p;

    @BeforeEach
    void setUp() {
        p = new Player(new Vector2(2, 2), new Vector2(1,0), 1, 0, null, null, 0);
    }

    @Test
    void getDamageable() {
        assertTrue(p.getDamageable());
    }

    @Test
    void getHealth() {
        assertTrue(p.getHealth() == p.getMaxHealth());
    }

    @Test
    void setHealth() {
        p.setHealth(30);
        assertTrue(p.getHealth() == 30);
    }

    @Test
    void isAlive() {
        assertTrue(p.isAlive());
        p.setHealth(0);
        assertFalse(p.isAlive());
    }

    @Test
    void getPhase() {
        assertTrue(p.getPhase() == 0);
    }

    @Test
    void setPhase() {
        p.setPhase(1);
        assertTrue(p.getPhase() == 1);
    }

    @Test
    void getVisible() {
        assertTrue(p.getVisible());
    }

    @Test
    void getPos() {
        assertTrue(p.getPos().equals(new Vector2(2,2)));
    }

    @Test
    void setPos() {
        p.setPos(new Vector2(0, 0));
        assertTrue(p.getPos().equals(new Vector2(0, 0)));
    }

    @Test
    void getMaxHealth() {
        assertTrue(p.getMaxHealth() == 100);
    }

    @Test
    void damage() {
        p.setHealth(p.getHealth());
        p.damage(30);
        assertTrue(p.getHealth()==p.getMaxHealth()-30);
    }

    @Test
    void getID() {
        assertTrue(p.getID() == 0);
    }

    @Test
    void equals() {
        Player p1 = new Player(null, null, 1, 1, null, null, 0);
        assertTrue(p.equals(p1));
    }

    @Test
    void setID() {
        p.setID(1);
        assertTrue(p.getID() == 1);
    }

}