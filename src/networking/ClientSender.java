package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static networking.Connection.out;

/**
 * Created by theooos on 21/01/2017.
 */
public class ClientSender {

    private ObjectOutputStream toServer;

    public ClientSender(ObjectOutputStream toServer){
        this.toServer = toServer;
    }

    public void send(Sendable obj){
        try {
            toServer.writeObject(obj);
            toServer.flush();
        } catch (IOException e) {
            out("Failed to send "+obj);
        }

    }

    public void close() throws IOException{
        toServer.close();
    }
}
