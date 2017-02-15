package client;

import client.ClientLogic.ClientComponent;
import client.ClientLogic.PlayerConnection;
import client.graphics.GameRenderer;
import client.ui.Display;
import networking.Connection;

/**
 * The class to be run to start the Client.
 */
public class Client {

    public Connection connection = new Connection();

    public Client(){
        connection.addFunctionEvent("String", Client::connectUI);
        connection.addFunctionEvent("Player", Client::out);
        connection.addFunctionEvent("AIPlayer", Client::out);
        connection.addFunctionEvent("Zombie", Client::out);
        connection.addFunctionEvent("Projectile", Client::out);

        PlayerConnection p = new PlayerConnection(connection);
        ClientComponent com = new ClientComponent(connection,p);
    }

    public static void connectUI(Object o) {
            String s = o.toString();

            switch (s) {
                //case "Connection made to server.":
                //case "You are being cared for by the lobby manager.":
                //case "Player connected":
                //case "You are in a 2 player lobby with 1 players in it":
                //case "Minimum number of players is reached, countdown starting":
                /*
                case "Game loading....":


                    break;
                 */
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
