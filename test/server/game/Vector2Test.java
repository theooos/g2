package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test vectors
 */
class Vector2Test {
    private Vector2 v0;
    private Vector2 v1;
    @BeforeEach
    void setUp() {
        v1 = new Vector2(1, 1);
        v0 = new Vector2(0, 1);
    }

    @Test
    void getX() {
        assertTrue(v1.getX() == 1);
    }

    @Test
    void getY() {
        assertTrue(v1.getY() == 1);
    }

    @Test
    void setX() {
        v1.setX(2);
        assertTrue(v1.getX() == 2);
    }

    @Test
    void setY() {
        v1.setY(2);
        assertTrue(v1.getY() == 2);
    }

    @Test
    void getDistanceTo() {
        assertTrue(v1.getDistanceTo(v0) == 1);
    }

    @Test
    void normalise() {
        assertTrue(v0.normalise().equals(v0));
        assertTrue(v1.normalise().equals(new Vector2(0.70710677f, 0.70710677f)));
    }

    @Test
    void vectorTowards() {
        assertTrue(v0.vectorTowards(v1).equals(new Vector2(1, 0)));
    }

    @Test
    void vectorTowards1() {
        assertTrue(v0.vectorTowards(1, 1).equals(new Vector2(1, 0)));
    }

    @Test
    void abs() {
        assertTrue(v1.abs() == (float)Math.sqrt(2));
        assertTrue(v0.abs() == 1);
    }

    @Test
    void mult() {
        assertTrue(v1.mult(v0).equals(v0));
    }

    @Test
    void mult1() {
        assertTrue(v1.mult(4).equals(new Vector2(4, 4)));
    }

    @Test
    void div() {
        assertTrue(v1.div(new Vector2(2, 2)).equals(new Vector2(0.5f, 0.5f)));
    }

    @Test
    void add() {
        assertTrue(v1.add(v0).equals(new Vector2(1, 2)));
    }

    @Test
    void sub() {
        assertTrue(v1.sub(v0).equals(new Vector2(1, 0)));
    }

    @Test
    void dot() {
        assertTrue(v1.dot(v0) == 2.0f);
    }

    @Test
    void toStringTest() {
        assertTrue(v1.toString().equals("(1.0, 1.0)"));
    }

    @Test
    void equals() {
        assertTrue(v1.equals(new Vector2(1, 1)));
    }

    @Test
    void toPoint() {
        Point p = v1.toPoint();
        assertTrue(p.equals(new Point(1, 1)));
    }


    @Test
    void clampedTo() {
        assertTrue(v0.clampedTo(1).equals(v0));
    }


    @Test
    void randomVector() {
        v1 = Vector2.randomVector(4);
        assertTrue(v1.getX() < 4);
        assertTrue(v1.getY() < 4);
    }

}