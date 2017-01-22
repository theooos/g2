package server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static server.Server.out;

/**
 * Created by theooos on 21/01/2017.
 */
public class NewConnectionListener extends Thread {

    boolean alive = true;

    private ServerSocket serverSocket;

    private ArrayList<ConnectionToClient> waitingClients = new ArrayList<>();

    public NewConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.start();
    }

    public void run(){
        //Keep getting clients and adding them to waitingClients
        try {
            while(alive) {
                // Hangs here until connection appears.
                out("Waiting for new connection...");
                Socket s = serverSocket.accept();
                ConnectionToClient conn = new ConnectionToClient(s);
                addWaitingClient(conn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addWaitingClient(ConnectionToClient conn) {
        waitingClients.add(conn);
    }

    public synchronized ConnectionToClient getClient(){
        if(waitingClients.size() != 0){
            ConnectionToClient nextClient = waitingClients.get(0);
            waitingClients.remove(0);
            return nextClient;
        }
        else return null;
    }

    public void kill(){
        alive = false;
    }
}
