package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by peran on 3/25/17.
 * Tests to see if the server can run
 */
class ServerTest {
    @Test
    void run() {
        Server.main(new String[]{});
    }

}