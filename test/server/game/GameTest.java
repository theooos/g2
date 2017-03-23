package server.game;

import networking.Connection_Server;
import objects.LobbyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

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
        connections.put(0, new Connection_Server(null));
        game = new Game(connections, 4, 4, new LobbyData(null, 5));
    }

    @Test
    void run() {
        //checks to see if the game runs
        game.run();
        game.stop();
    }

}