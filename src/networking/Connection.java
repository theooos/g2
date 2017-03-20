package networking;

import client.Client;
import client.ClientSettings;
import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * This holds the connection between Server and Client, and can be used by either.
 */
public class Connection {

    private Socket socket;
    private NetworkSender toConnection;
    private NetworkListener fromConnection;
    private NetworkEventHandler handler = new NetworkEventHandler();

    /**
     * FOR USE ONLY BY THE CLIENT. Initialises the connection the server.
     */
    public Connection(Client client) throws IOException {
        if (establishSocket()) establishConnection(client);
    }

    /**
     * FOR USE ONLY BY SERVER. Initialises the connection to a client.
     *
     * @param socket The server socket.
     */
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        out("Connection made to client.");
        establishConnection();
    }

    /**
     * Generates the socket.
     */
    private boolean establishSocket() throws IOException {
        int attempts = 3;

        String HOSTNAME = ClientSettings.LOCAL ? "localhost" : ClientSettings.SERVER_IP;

        for (int i = 1; i <= attempts; i++) {
            socket = new Socket(HOSTNAME, ClientSettings.PORT);
            return true;
        }
        return false;
    }

    /**
     * Creates the input and output streams.
     */
    private boolean establishConnection() throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
        fromConnection = new NetworkListener(new ObjectInputStream(socket.getInputStream()), handler, null);

        out("Connection made to server.");
        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        return true;
    }

    /**
     * Creates the input and output streams.
     * @param client
     */
    private boolean establishConnection(Client client) throws IOException {
        toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
        fromConnection = new NetworkListener(new ObjectInputStream(socket.getInputStream()), handler, client);

        out("Connection made to server.");
        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        return true;
    }

    /**
     * Closes all streams.
     */
    public void closeConnection() {
        try {
            toConnection.close();
            fromConnection.close();
            socket.close();
        } catch (IOException e) {
            out("Could not close connection gracefully.");
        }
    }

    /**
     * Creates a ServerSocket for use by the server.
     *
     * @return The socket.
     */
    public static ServerSocket getServerSocket() {
        try {
            return new ServerSocket(ClientSettings.PORT);
        } catch (IOException e) {
            out("Failed to connect through server socket.");
        }
        return null;
    }

    /**
     * Sends a Sendable object the partner.
     *
     * @param obj Sendable item.
     */
    public void send(Sendable obj) {
        toConnection.queueForSending(obj);
    }

    public void addFunctionEvent(String className, Consumer<Sendable> consumer) {
        handler.addFunction(className, consumer);
    }

    /**
     * For debugging use only. Remove all references for production.
     *
     * @param o Object to print.
     */
    static void out(Object o) {
        if (ClientSettings.DEBUG) System.out.println("[NETWORK] " + o);
    }
}
