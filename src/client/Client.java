package client;

import client.ui.Display;
import networking.Connection;

/**
 * The class to be run to start the Client.
 */
public class Client {

    private Connection connection = new Connection();

    public Client(){
        connection.addFunctionEvent("String", Client::connectUI);
        connection.addFunctionEvent("Player", Client::out);
        connection.addFunctionEvent("AIPlayer", Client::out);
        connection.addFunctionEvent("Zombie", Client::out);
        connection.addFunctionEvent("Projectile", Client::out);
    }

    public static void connectUI(Object o) {
            String s = o.toString();

            switch (s) {
                //case "Connection made to server.":
                //case "You are being cared for by the lobby manager.":
                //case "Player connected":
                //case "You are in a 2 player lobby with 1 players in it":
                //case "Minimum number of players is reached, countdown starting":
                default:
                    System.out.println("[CLIENT] LOL" + s);
                    break;
            }
    }

    public static void main(String[] args){

        Display display = new Display();
        display.displayUI();
    }

    public static void out(Object o){
            System.out.println("[CLIENT] " + o);
    }

}
