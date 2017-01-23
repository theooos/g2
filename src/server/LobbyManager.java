package server;

import objects.String;
import networking.Connection;

/**
 * Created by theooos on 21/01/2017.
 */
public class LobbyManager {

    public void addConnection(Connection newClient) {
        newClient.send(new String("You have being cared for by the lobby manager."));
    }

    private static void out(Object o) {
        System.out.println(o);
    }
}
