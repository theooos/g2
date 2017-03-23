package networking;

import client.Client;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;


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
                System.out.println("Connection broke. Performing shutdown.");
                performShutdown();
            } catch (ClassNotFoundException e) {
                System.out.println("NetworkListener failed to interpret object type.");
                performShutdown();
            }
        }
    }

    abstract void performShutdown();
}
