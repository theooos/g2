package objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test phase switch objects
 */
class PhaseObjectTest {
    @Test
    void getID() {
        PhaseObject p = new PhaseObject(1);
        assertTrue(p.getID() == 1);
    }

}