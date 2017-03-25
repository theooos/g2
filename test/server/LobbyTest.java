package server;

import networking.Connection_Server;
import org.junit.jupiter.api.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by peran on 3/25/17.
 * Used to test the lobby manager
 */
class LobbyTest {

    @Test
    void isFull() {
        Lobby l = new Lobby(0, 10);
        assertTrue(l.isFull());
    }

    @Test
    void addConnection1() {
        Lobby l = new Lobby(2, 10);
        l.addConnection(new Connection_Server(null));
    }

    @Test
    void isGameRunning() {
        Lobby l = new Lobby(2, 0);
        assertFalse(l.isGameRunning());
        l.addConnection(new Connection_Server(null));
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                assertTrue(l.isGameRunning());
            }
        }, 2000);
    }
}