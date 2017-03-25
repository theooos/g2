package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used for testing switch objects
 */
class SwitchObjectTest {
    SwitchObject s;

    @BeforeEach
    void setUp() {
        s = new SwitchObject(0, true);
    }

    @Test
    void takeWeaponOneOut() {
        assertTrue(s.takeWeaponOneOut());
    }

    @Test
    void getID() {
        assertTrue(s.getID() == 0);
    }

}