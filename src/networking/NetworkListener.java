package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;

import static networking.Connection.out;


/**
 * Listens for communications on the network. Then deals with them appropriately?
 */
class NetworkListener implements Runnable {

    private final NetworkEventHandler handler;
    private ObjectInputStream fromConnection;
    private boolean running = true;

    NetworkListener(ObjectInputStream fromConnection, NetworkEventHandler handler){
        this.fromConnection = fromConnection;
        this.handler = handler;
        new Thread(this).start();
    }

    /**
     * Blocks connection when waiting for a new object. Hence this is threaded.
     */
    public void run(){
        while(running){
            try {
                Sendable received = (Sendable) fromConnection.readObject();
                System.out.println("I RECEIVED A THING: " + received);
                handler.queueForExecution(received);
            } catch (IOException e) {
                out("NetworkListener's connection with the server broke.");
                out(e.getMessage());
                running = false;
            } catch (ClassNotFoundException e) {
                out("NetworkListener failed to interpret object type.");
                out(e.getMessage());
            }
        }
    }

    /**
     * Closes the input stream.
     * @throws IOException
     */
    void close() throws IOException {
        running = false;
        fromConnection.close();
    }
}
