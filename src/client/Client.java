package client;

import client.ClientLogic.ClientReceiver;
import client.ui.Display;
import networking.Connection;
import networking.SendingTester;

/**
 * The class to be run to start the Client.
 */
public class Client {

    public Connection connection = new Connection();

    public Client() {
        ClientReceiver cr = new ClientReceiver(connection);
//        new Thread(new SendingTester(connection)).start();
    }

    public static void main(String[] args) {
        Display display = new Display();
        display.displayUI();
    }
}
