package networking;

import client.Client;
import objects.Sendable;
import server.game.Player;

import java.io.IOException;
import java.io.ObjectInputStream;

import static networking.Connection.out;


/**
 * Listens for communications on the network. Then deals with them appropriately?
 */
abstract class NetworkListener implements Runnable {

    Client client;
    NetworkEventHandler handler;
    ObjectInputStream fromConnection;
    boolean isRunning;

    /**
     * Blocks connection when waiting for a new object. Hence this is threaded.
     */
    public void run(){
        isRunning = true;
        while(isRunning){
            try {
                Sendable received = (Sendable) fromConnection.readObject();
                handler.queueForExecution(received);
            } catch (IOException e) {
                out("Connection_Server broke. Performing shutdown.");
                performShutdown();
            } catch (ClassNotFoundException e) {
                out("NetworkListener failed to interpret object type.");
                performShutdown();
            }
        }
    }

    /**
     * Closes the input stream.
     * @throws IOException
     */
    void close() throws IOException {
        isRunning = false;
        fromConnection.close();
    }

    abstract void performShutdown();
}
