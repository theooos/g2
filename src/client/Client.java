package client;

import client.ui.Display;
import networking.Connection;

/**
 * The class to be run to start the Client.
 */
public class Client {

    Connection connection = new Connection();

    public Client(){
        connection.addFunctionEvent("String", Client::out);
        connection.addFunctionEvent("Player", Client::out);
        connection.addFunctionEvent("AIPlayer", Client::out);
        connection.addFunctionEvent("Zombie", Client::out);
        connection.addFunctionEvent("Projectile", Client::out);

        //testing branch
    }

    public static void main(String[] args){
        Client client = new Client();

        Display d = new Display();
        d.displayUI(client.connection);
    }

    public static void out(Object o){
        System.out.println("[CLIENT] "+o);
    }
}
