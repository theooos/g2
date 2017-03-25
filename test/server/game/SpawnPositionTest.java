package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test spawnpoints
 */
class SpawnPositionTest {
    SpawnPosition s;
    @BeforeEach
    void setUp() {
        s = new SpawnPosition(new Vector2(1, 1), 0);
    }

    @Test
    void getPos() {
        assertTrue(s.getPos().equals(new Vector2(1, 1)));
    }

    @Test
    void getTeam() {
        assertTrue(s.getTeam() == 0);
    }

}