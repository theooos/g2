package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static server.Server.out;

/**
 * Created by theooos on 18/01/2017.
 */
public class Connection {

    private static String HOSTNAME = "localhost";
    private static int PORT = 3000;

    private Socket socket;
    private ClientSender toClient;
    private ClientListener fromClient;

    /**
     * To initialise the server.
     */
    public Connection(){
        try {
            socket = new Socket(HOSTNAME,PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        establishConnection();
    }

    /**
     * To initialise client connection.
     * @param socket
     */
    public Connection(Socket socket) {
        this.socket = socket;
        out("Connection made to client.");
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

    public static void out(Object o){
        System.out.println("[NETWORK] "+o);
    }
}
