package networking;

import java.io.IOException;
import java.io.ObjectInputStream;

import static networking.Connection.out;


/**
 * Listens for communications on the network. Then deals with them appropriately?
 */
class NetworkListener extends Thread {

    private ObjectInputStream fromConnection;
    private boolean running = true;

    NetworkListener(ObjectInputStream fromConnection){
        this.fromConnection = fromConnection;
        this.start();
    }

    /**
     * Blocks connection when waiting for a new object. Hence this is threaded.
     */
    public void run(){
        while(running){
            try {
                Object received = fromConnection.readObject();
                dealWithCommunication(received);
            } catch (IOException e) {
                out("NetworkListener broke attempting to read object.");
                out(e.getMessage());
                running = false;
            } catch (ClassNotFoundException e) {
                out("NetworkListener broke attempting to discover class type.");
                out(e.getMessage());
                running = false;
            }
        }
    }

    /**
     * TODO This dictates what should happen when a particular type is received.
     * @param received The received object.
     */
    private void dealWithCommunication(Object received){
        if (received instanceof objects.String) out(received);
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
