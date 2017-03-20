package server;

import networking.Connection;
import networking.Connection_Server;
import objects.String;

import java.util.ArrayList;


class LobbyManager {

    private ArrayList<Lobby> lobbies;

    LobbyManager() {
        lobbies = new ArrayList<>();
        lobbies.add(createLobby());
    }

    void addConnection(Connection_Server c) {

        c.addFunctionEvent("String", Server::out);
        try {
            c.send(new String("You are being cared for by the lobby manager."));
        } catch (Exception e) {
            System.out.println("Dude didn't make it into a lobby before dying.");
            return;
        }

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
        return new Lobby(4);
    }
}
