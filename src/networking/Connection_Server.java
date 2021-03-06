package networking;

import client.Client;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by theo on 20/03/2017.
 * A connection class used by the server
 */
public class Connection_Server extends Connection {
    private int ID;

    public Connection_Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void initialise() throws IOException {
        establishConnection();
    }

    @Override
    boolean establishConnection(Client... clients) throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()), running);
        fromConnection = new NetworkListener_Server(new ObjectInputStream(socket.getInputStream()), handler, running);

        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        System.out.println("Connection established with client.");
        return true;
    }

    public void send(Sendable sendable) throws Exception {
        try {
            toConnection.queueForSending(sendable);
        }
        catch(NullPointerException e) {
            System.out.println("Uh oh, no socket.  Testing?");
        }
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
