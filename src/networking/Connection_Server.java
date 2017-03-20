package networking;

import client.Client;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by theo on 20/03/2017.
 */
public class Connection_Server extends Connection {

    public Connection_Server(Socket socket) throws IOException {
        this.socket = socket;
        establishConnection();
    }

    @Override
    boolean establishConnection(Client... clients) throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
        fromConnection = new NetworkListener_Server(new ObjectInputStream(socket.getInputStream()), handler);

        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        out("Connection established with client.");
        return true;
    }

    public void send(Sendable sendable) throws Exception {
        toConnection.queueForSending(sendable);
    }
}
