package server;

import networking.Connection;
import networking.NewConnectionListener;

import java.net.ServerSocket;

/**
 * The class to be run to start the server.
 */
public class Server extends Thread {

    private ServerSocket serverSocket = Connection.getServerSocket();
    private NewConnectionListener newConnectionListener = new NewConnectionListener(serverSocket);

    private LobbyManager lobbyManager = new LobbyManager();

    /**
     * Always checking if the listener has any new clients. If so, sends them to the lobby manager.
     */
    public void run(){
        while(true){
            // Keep checking if listener has any new clients, send them to lobby manager if so.
            Connection newClient = newConnectionListener.getClient();
            if(newClient != null){
                lobbyManager.addConnection(newClient);
            }
        }
    }

    public static void main(String[] args){
        new Server().start();
    }

    public static void out(Object o){
        System.out.println("[SERVER] "+o);
    }
}
