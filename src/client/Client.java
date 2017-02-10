package client;

import networking.Connection;

/**
 * The class to be run to start the Client.
 */
public class Client {

    static Connection connection = new Connection();

    public Client(){
        connection.addFunctionEvent("String", Client::out);
        connection.addFunctionEvent("Player", Client::out);
        connection.addFunctionEvent("AIPlayer", Client::out);
        connection.addFunctionEvent("Zombie", Client::out);
        connection.addFunctionEvent("Projectile", Client::out);

        //testing branch
    }

    public static void main(String[] args){
        new Client();
        SendMove sm = new SendMove(connection);





    }

    public static void out(Object o){
        System.out.println("[CLIENT] "+o);
    }
}
