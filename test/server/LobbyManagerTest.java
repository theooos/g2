package server;

import networking.Connection_Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Used to test the lobby manager
 */
class LobbyManagerTest {
    @Test
    void addConnection() {
        LobbyManager lm = new LobbyManager(new String[]{});
        lm.addConnection(new Connection_Server(null));
        lm = new LobbyManager(new String[]{"2"});
        lm.addConnection(new Connection_Server(null));
        lm = new LobbyManager(new String[]{"2", "10"});
        lm.addConnection(new Connection_Server(null));
    }

}