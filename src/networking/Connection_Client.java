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

    private Client client;

    public Connection_Client(Client client) {
        this.client = client;
    }

    @Override
    public void initialise() throws IOException {
        if (establishSocket()) establishConnection(client);
    }

    @Override
    boolean establishConnection(Client... clients) throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()), running);
        fromConnection = new NetworkListener_Client(new ObjectInputStream(socket.getInputStream()), handler, clients[0], running);

        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        System.out.println("Connection established with server.");
        return true;
    }

    public void send(Sendable sendable) {
        toConnection.queueForSending(sendable);
    }
}
