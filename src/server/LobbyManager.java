package server;

import objects.String;
import server.networking.ConnectionToClient;

/**
 * Created by theooos on 21/01/2017.
 */
public class LobbyManager {

    public void addConnection(ConnectionToClient newClient) {
        newClient.send(new String("You have being cared for by the lobby manager."));
    }

    private static void out(Object o) {
        System.out.println(o);
    }
}
