package client;

import networking.Connection;

/**
 * The class to be run to start the Client.
 */
public class Client {

    Connection connection = new Connection();

    public Client(){
        connection.addFunctionEvent("String", Client::out);
    }

    public static void main(String[] args){
        Client client = new Client();
    }

    public static void out(Object o){
        System.out.println("[CLIENT] "+o);
    }
}
