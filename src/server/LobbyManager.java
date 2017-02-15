package server;

import networking.Connection;
import objects.String;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by theooos on 21/01/2017.
 */
public class LobbyManager {

    private ArrayList<Lobby> lobbies;

    private boolean gameOn;

    public LobbyManager() {
        lobbies = new ArrayList<>();
        lobbies.add(createLobby());
    }

    public void addConnection(Connection c) {

        //c.addFunctionEvent("String", LobbyManager::messageReceived);
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

    private static void messageReceived(Object o) {
        System.out.println("[ FROM CLIENT ] " + o);
    }

    private Lobby createLobby() {
        Random r = new Random();
        int size = r.nextInt(4)+1;
        size = size*4;
        return new Lobby(2);
    }

    private void out(Object o) {
        System.out.println(o);
    }
}
