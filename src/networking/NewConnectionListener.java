package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static networking.Connection.out;

/**
 * Creates the listening thread for the Server which accepts new clients.
 */
public class NewConnectionListener implements Runnable{

    boolean alive = true;
    private ServerSocket serverSocket;
    private ArrayList<Connection> waitingClients = new ArrayList<>();

    /**
     * Constructor.
     * @param serverSocket The socket that the thread should listen on.
     */
    public NewConnectionListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        new Thread(this).start();
    }

    /**
     * Waiting for a new connection is blocking, hence threaded.
     */
    public void run(){
        try {
            while(alive) {
                // Hangs here until connection appears.
                out("Waiting for new connection...");
                Socket s = serverSocket.accept();
                Connection conn = new Connection_Server(s);
                addWaitingClient(conn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a connection to an ArrayList in a synchronised way, ready for another class to pop.
     * @param conn The connection to add.
     */
    private synchronized void addWaitingClient(Connection conn) {
        waitingClients.add(conn);
    }

    /**
     * This pops the next connection from the ArrayList.
     * @return The new connection.
     */
    public synchronized Connection getClient(){
        if(waitingClients.size() != 0){
            Connection nextClient = waitingClients.get(0);
            waitingClients.remove(0);
            return nextClient;
        }
        else return null;
    }

    /**
     * Kills the thread.
     */
    public void kill(){
        alive = false;
    }
}
