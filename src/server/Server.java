package server;

import networking.Connection;
import networking.Connection_Server;
import networking.NewConnectionListener;

import java.net.ServerSocket;

/**
 * The class to be run to start the server.
 */
public class Server extends Thread {

    private ServerSocket serverSocket = Connection.getServerSocket();
    private NewConnectionListener newConnectionListener = new NewConnectionListener(serverSocket);

    private static LobbyManager lobbyManager;
    private static String[] arg;

    /**
     * Always checking if the listener has any new clients. If so, sends them to the lobby manager.
     */
    public void run(){
        while(true){
            // Keep checking if listener has any new clients, send them to lobby manager if so.
            Connection_Server newClient = newConnectionListener.getClient();
            if(newClient != null){
//                new Thread(new SendingTester(newClient)).start();
                lobbyManager.addConnection(newClient);
            }
        }
    }

    public static void main(String[] args){
        arg = args;
        lobbyManager = new LobbyManager(arg);
        new Server().start();
    }
}
