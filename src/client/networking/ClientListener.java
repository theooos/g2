package client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;

import static client.Client.out;

/**
 * Created by theooos on 21/01/2017.
 */
public class ClientListener extends Thread {

    private ObjectInputStream fromServer;
    private boolean running = true;

    public ClientListener(ObjectInputStream fromServer){
        this.fromServer = fromServer;
        this.start();
    }

    public void run(){
        while(running){
            try {
                Object received = fromServer.readObject();
                if (received instanceof objects.String) out(received);
            } catch (IOException e) {
                out("ClientListener broke attempting to read object.");
                out(e.getMessage());
                running = false;
            } catch (ClassNotFoundException e) {
                out("ClientListener broke attempting to discover class type.");
                out(e.getMessage());
                running = false;
            }
        }
    }

    public void close() throws IOException {
        running = false;
        fromServer.close();
    }
}
