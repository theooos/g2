package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test powerups
 */
class PowerUpTest {
    PowerUp h;
    PowerUp w;
    @BeforeEach
    void setUp() {
        h = new PowerUp(new Vector2(1, 1), PowerUp.Type.health, 0, 0);
        w = new PowerUp(new Vector2(1, 5), PowerUp.Type.heat, 1, 0);
    }

    @Test
    void isChanged() {
        assertFalse(h.isChanged());
    }

    @Test
    void setChanged() {
        h.setChanged(true);
        assertTrue(h.isChanged());
    }

    @Test
    void getType() {
        assertFalse(h.getType() == PowerUp.Type.heat);
        assertTrue(w.getType() == PowerUp.Type.heat);
    }

}