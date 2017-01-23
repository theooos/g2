package server;

import networking.Connection;
import networking.NewConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by theooos on 18/01/2017.
 */
public class Server extends Thread {

    private static int PORT = 3000;

    private ServerSocket serverSocket;

    private LobbyManager lobbyManager = new LobbyManager();
    private NewConnectionListener newConnectionListener;

    public Server(){
        connect();
        newConnectionListener = new NewConnectionListener(serverSocket);
        this.start();
    }

    public void run(){
        while(true){
            // Keep checking if listener has any new clients, send them to lobby manager if so.
            Connection newClient = newConnectionListener.getClient();
            if(newClient != null){
                lobbyManager.addConnection(newClient);
            }
        }
    }

    private void connect() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            out("Failed to connect through server socket.");
        }
    }

    public static void kill(){
        System.exit(0);
    }

    public static void main(String[] args){
        Server server = new Server();
    }

    public static void out(Object o){
        System.out.println("[SERVER] "+o);
    }
}
