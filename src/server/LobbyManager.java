package server;

import objects.String;
import networking.Connection;

/**
 * Created by theooos on 21/01/2017.
 */
public class LobbyManager {

    public void addConnection(Connection newClient) {
        newClient.send(new String("You have are cared for by the lobby manager."));
        for(int i = 0; i < 10; i++){
            newClient.send(new String("BANTER TEST PACE" + i));
        }
    }

    private static void out(Object o) {
        System.out.println(o);
    }
}
