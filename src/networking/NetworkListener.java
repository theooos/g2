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

    ReferenceBool running;

    /**
     * Blocks connection when waiting for a new object. Hence this is threaded.
     */
    public void run(){
        while(running.value){
            try {
                Sendable received = (Sendable) fromConnection.readObject();
                handler.queueForExecution(received);
            } catch (IOException e) {
                out("Connection broke. Performing shutdown.");
                performShutdown();
            } catch (ClassNotFoundException e) {
                out("NetworkListener failed to interpret object type.");
                performShutdown();
            }
        }
    }

    abstract void performShutdown();
}
