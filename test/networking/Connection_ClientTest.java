package networking;

import client.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Created by theo on 23/03/2017.
 */
class Connection_ClientTest {
    Connection_Client connection_client;
    Client client;

    @BeforeEach
    void setUp() {
        client = mock(Client.class);
        connection_client = new Connection_Client(client);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void establishConnection() {
        assertNull(connection_client.toConnection);
        assertNull(connection_client.fromConnection);
        try {
            connection_client.establishConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(connection_client.toConnection);
        assertNotNull(connection_client.fromConnection);
    }

    @Test
    void send() {

    }

    @Test
    void establishSocket() {

    }

    @Test
    void establishConnection1() {

    }

    @Test
    void getServerSocket() {

    }

    @Test
    void addFunctionEvent() {

    }

}