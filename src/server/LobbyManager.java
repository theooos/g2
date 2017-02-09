package server;

import objects.String;
import networking.Connection;
import java.util.ArrayList;
import java.util.Random;

public class LobbyManager {

    private ArrayList<Lobby> lobbies;

    public LobbyManager() {
        lobbies = new ArrayList<>();
        lobbies.add(createLobby());
    }

    public void addConnection(Connection c) {
        c.send(new String("You are being cared for by the lobby manager."));

        boolean added = false;

        for (Lobby l: lobbies) {
            if (!l.isFull()) {
               l.addConnection(c);
               added = true;
               break;
            }
        }

        if (!added) {
            Lobby l = createLobby();
            l.addConnection(c);
            lobbies.add(l);
        }
    }

    private Lobby createLobby() {
        Random r = new Random();
        int size = r.nextInt(4)+1;
        size = size*4;
        return new Lobby(2);
    }
}
