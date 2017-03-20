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
public abstract class Connection {

    Socket socket;
    NetworkSender toConnection;
    NetworkListener fromConnection;
    NetworkEventHandler handler = new NetworkEventHandler();

    Boolean running = true;

    /**
     * Generates the socket.
     */
    boolean establishSocket() throws IOException {
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
    abstract boolean establishConnection(Client... clients) throws IOException;

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
