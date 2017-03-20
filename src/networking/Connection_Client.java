package networking;

import client.Client;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by theo on 20/03/2017.
 */
public class Connection_Client extends Connection {

    public Connection_Client(Client client) throws IOException {
        if (establishSocket()) establishConnection(client);
    }

    @Override
    boolean establishConnection(Client... clients) throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
        fromConnection = new NetworkListener_Client(new ObjectInputStream(socket.getInputStream()), handler, clients[0]);

        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        out("Connection established with server.");
        return true;
    }

    public void send(Sendable sendable){
        toConnection.queueForSending(sendable);
    }
}
