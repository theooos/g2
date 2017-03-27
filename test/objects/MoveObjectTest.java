package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.game.Vector2;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used for testing move objects
 */
class MoveObjectTest {
    private MoveObject m;

    @BeforeEach
    void setUp() {
        m = new MoveObject(new Vector2(1,1), new Vector2(1,0), 0, 3);
    }

    @Test
    void getPos() {
        assertTrue(m.getPos().equals(new Vector2(1, 1)));
    }

    @Test
    void getDir() {
        assertTrue(m.getDir().equals(new Vector2(1, 0)));
    }

    @Test
    void getID() {
        assertTrue(m.getID() == 0);
    }

    @Test
    void toStringTest() {
        assertTrue(m.toString().equals("Pos: " + m.getPos() + " Dir: " + m.getDir() + " ID: " + m.getID()));
    }

    @Test
    void getMoveCounter() {
        assertTrue(m.getMoveCounter() == 3);
    }

}