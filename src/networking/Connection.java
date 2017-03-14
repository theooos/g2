package networking;

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

    private static boolean debug = true;

    /**
     * FOR USE ONLY BY THE CLIENT. Initialises the connection the server.
     */
    public Connection() throws IOException {
        if (establishSocket()) establishConnection();
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
        int attempts = 3;

        retries:
        for (int i = 1; i <= attempts; i++) {
            toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
            fromConnection = new NetworkListener(new ObjectInputStream(socket.getInputStream()), handler);
            break retries;
        }

        out("Connection made to server.");
        new Thread(handler).start();
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        return true;
    }

    /**
     * Closes all streams.
     */
    private void closeConnection() {
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

    /**
     * This re-initialises the connection in case of an error.
     */
    public void resetConnection() {
        closeConnection();
        try {
            establishConnection();
        } catch (Exception e) {
            System.err.println("Failed to reset connection.");
        }
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
        if (debug) System.out.println("[NETWORK] " + o);
    }
}
