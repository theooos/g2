package client.networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static client.Client.out;

/**
 * Created by theooos on 18/01/2017.
 */
public class ConnectionToServer{

    private static String HOSTNAME = "localhost";
    private static int PORT = 3000;

    private Socket serverSocket;
    private ClientSender toServer;
    private ClientListener fromServer;

    public ConnectionToServer(){
        establishConnection();
    }

    private void establishConnection(){
        try {
            serverSocket = new Socket(HOSTNAME,PORT);
            toServer = new ClientSender(new ObjectOutputStream(serverSocket.getOutputStream()));
            fromServer = new ClientListener(new ObjectInputStream(serverSocket.getInputStream()));
        } catch (IOException e) {
            out("Failed to establish connection.");
        }
        out("Connection made to server.");
    }

    private void closeConnection(){
        try {
            toServer.close();
            fromServer.close();
            serverSocket.close();
        } catch (IOException e) {
            out("Could not close connection gracefully.");
        }
    }

    public void send(Sendable obj){
        toServer.send(obj);
    }

    public void resetConnection(){
        closeConnection();
        establishConnection();
    }
}
