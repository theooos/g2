package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Line2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test walls
 */
class WallTest {
    Wall w;

    @BeforeEach
    void setUp() {
        w = new Wall(new Vector2(10,10), new Vector2(10, 100), 0, false, false);
    }

    @Test
    void getStartPos() {
        assertTrue(w.getStartPos().equals(new Vector2(10, 10)));
    }

    @Test
    void getEndPos() {
        assertTrue(w.getEndPos().equals(new Vector2(10, 100)));
    }

    @Test
    void damageWall() {
        int health = w.getHealth();
        w.damage(10);
        assertTrue(w.getHealth() == health-10);
    }

    @Test
    void intact() {
        assertTrue(w.intact());
        w.damage(w.getMaxHealth());
        assertFalse(w.intact());
    }

    @Test
    void inPhase() {
        assertTrue(w.inPhase(0));
        assertFalse(w.inPhase(1));
    }

    @Test
    void toLine() {
        Line2D l = new Line2D.Float(10, 10, 10, 100);
        assertTrue(l.getP1().equals(w.getStartPos().toPoint()) && l.getP2().equals(w.getEndPos().toPoint()));
    }

    @Test
    void isBoundary() {
        assertFalse(w.isBoundary());
    }

}