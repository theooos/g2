package client;

import client.ClientLogic.ClientComponent;
import client.ClientLogic.ClientReceiver;
import client.ClientLogic.ClientSendable;
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
        ClientComponent com = new ClientComponent(connection);

    }

    public static void main(String[] args){

        Display display = new Display();
        display.displayUI();
    }
}
