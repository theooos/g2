package objects;

import org.junit.jupiter.api.Test;
import org.lwjgl.Sys;
import server.game.Scoreboard;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/27/17.
 * Used to test game over object
 */
class GameOverTest {
    @Test
    void getScoreboard() {
        Scoreboard sb = new Scoreboard(4, 3);
        GameOver g = new GameOver(new Scoreboard(4, 3));
        assertTrue(g.getScoreboard().equals(sb));
    }

}