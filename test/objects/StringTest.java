package objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test homemade string object
 */
class StringTest {
    @Test
    void toStringTest() {
        String s = new String("hello");
        assertTrue(s.toString().equals("hello"));
    }

}