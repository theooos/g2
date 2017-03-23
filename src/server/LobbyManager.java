package server;

import networking.Connection_Server;
import objects.String;

import java.util.ArrayList;


class LobbyManager {

    private ArrayList<Lobby> lobbies;
    private int maxSize;
    private int countDown;

    /**
     * Sets up a new lobby manager
     * @param args the arguments inc. lobby size and countdown
     */
    LobbyManager(java.lang.String[] args) {
        lobbies = new ArrayList<>();
        lobbies.add(createLobby());
        if (args.length == 2) {
            countDown = Integer.parseInt(args[1]);
            maxSize = Integer.parseInt(args[0]);
        } else if (args.length == 1) {
            maxSize = Integer.parseInt(args[0]);
            countDown = 5;
        } else {
            maxSize = 2;
            countDown = 0;
        }
    }

    /**
     * Adds a new connection to the lobby manager and allocates them a lobby
     * @param c the connection to send to a lobby
     */
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

    /**
     * Creates a new lobby
     * @return a lobby with correct maxSize and countDown
     */
    private Lobby createLobby() {
        return new Lobby(maxSize, countDown);
    }
}
