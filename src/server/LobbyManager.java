package server;

import networking.Connection;
import objects.String;

import java.util.ArrayList;


class LobbyManager {

    private ArrayList<Lobby> lobbies;

    LobbyManager() {
        lobbies = new ArrayList<>();
        lobbies.add(createLobby());
    }

    void addConnection(Connection c) {

        c.addFunctionEvent("String", Server::out);
        c.send(new String("You are being cared for by the lobby manager."));

        boolean added = false;

        for (Lobby l: lobbies) {
            if (!l.isFull() && !l.isGameRunning()) {
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
//        Random r = new Random();
//        int size = r.nextInt(4)+1;

        return new Lobby(4);
    }
}
