package client;

import networking.Connection;

/**
 * Created by theooos on 18/01/2017.
 */
public class Client {

    Connection connection = new Connection();

    public static void main(String[] args){
        Client client = new Client();
    }

    public static void out(Object o){
        System.out.println("[CLIENT] "+o);
    }
}
