package objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test lobby data
 */
class LobbyDataTest {
    private LobbyData l;

    @BeforeEach
    void setUp() {
        l = new LobbyData(new InitPlayer[]{new InitPlayer(0, new String("test"), true, 0)}, 1);
    }

    @Test
    void getPlayers() {
        assertTrue((new InitPlayer(0, new String("test"), true, 0)).equals(l.getPlayers()[0]));
    }

    @Test
    void getMapID() {
        assertTrue(l.getMapID() == 1);
    }

    @Test
    void equalsTest() {
        LobbyData ld = new LobbyData(new InitPlayer[]{new InitPlayer(0, new String("test"), true, 0)}, 1);
        assertTrue(ld.equals(l));
        ld = new LobbyData(new InitPlayer[]{new InitPlayer(0, new String("test"), false, 0)}, 1);
        assertFalse(ld.equals(l));
        ld = new LobbyData(new InitPlayer[]{}, 1);
        l = new LobbyData(new InitPlayer[]{}, 1);
        assertTrue(ld.equals(l));
    }

}