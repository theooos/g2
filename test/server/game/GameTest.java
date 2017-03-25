package server.game;

import networking.Connection_Server;
import objects.LobbyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/23/17.
 * Made to test the game loop
 */
class GameTest {
    private Game game;
    @BeforeEach
    void setUp() {
        HashMap<Integer, Connection_Server> connections = new HashMap<>();
        //connections.put(0, new Connection_Server(null));
        game = new Game(connections, 1, 1, new LobbyData(null, 5));
    }


    @Test
    void runTest() {
        //checks to see if the game runs
        game.run();
        game.stop();
        assertFalse(false);
    }

    @Test
    void gameTick() {
        //used to check the game can run without crashing
        game.gameTick();
        assertFalse(false);
    }


}