package server.networking;

import client.Client;
import client.networking.ClientListener;
import client.networking.ClientSender;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static server.Server.out;

/**
 * Created by theooos on 18/01/2017.
 */
public class ConnectionToClient {

    private Socket socket;
    private ClientSender toClient;
    private ClientListener fromClient;

    public ConnectionToClient(Socket socket) {
        this.socket = socket;
        out("ConnectionToClient made to client.");
        establishConnection();
    }

    private void establishConnection(){
        try {
            toClient = new ClientSender(new ObjectOutputStream(socket.getOutputStream()));
            fromClient = new ClientListener(new ObjectInputStream(socket.getInputStream()));
        } catch (IOException e) {
            out("Failed to establish connection.");
        }
        out("Connection made to server.");
    }

    private void closeConnection(){
        try {
            toClient.close();
            fromClient.close();
            socket.close();
        } catch (IOException e) {
            out("Could not close connection gracefully.");
        }
    }

    public void send(Sendable obj){
        toClient.send(obj);
    }

    public void resetConnection(){
        closeConnection();
        establishConnection();
    }
}
