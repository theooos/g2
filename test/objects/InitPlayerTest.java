package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Tests init player
 */
class InitPlayerTest {
    private InitPlayer p;

    @BeforeEach
    void setUp() {
        p = new InitPlayer(0, new String("test"), true, 0);
    }

    @Test
    void getID() {
        assertTrue(p.getID() == 0);
    }

    @Test
    void getName() {
        assertTrue(p.getName().toString().equals("test"));
    }

    @Test
    void isAI() {
        assertTrue(p.isAI());
    }

    @Test
    void getTeam() {
        assertTrue(p.getTeam() == 0);
    }

    @Test
    void equals() {
        assertTrue(p.equals(new InitPlayer(0, new String("test"), true, 0)));
    }

}