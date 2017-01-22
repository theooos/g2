package client;

import client.networking.ConnectionToServer;

/**
 * Created by theooos on 18/01/2017.
 */
public class Client {

    ConnectionToServer connection = new ConnectionToServer();

    public static void main(String[] args){
        Client client = new Client();
    }

    public static void out(Object o){
        System.out.println("[CLIENT] "+o);
    }
}
