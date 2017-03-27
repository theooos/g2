package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test fire object
 */
class FireObjectTest {
    private FireObject f;

    @BeforeEach
    void setUp() {
        f = new FireObject(0, true);
    }

    @Test
    void isStartFire() {
        assertTrue(f.isStartFire());
    }

    @Test
    void getPlayerID() {
        assertTrue(f.getPlayerID() == 0);
    }

}