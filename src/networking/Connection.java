package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

/**
 * This holds the connection between Server and Client, and can be used by either.
 */
public class Connection {

    private static int PORT = 3000;
    private static final boolean LOCAL = true;

    private Socket socket;
    private NetworkSender toConnection;
    private NetworkListener fromConnection;
    private NetworkEventHandler handler = new NetworkEventHandler();

    private static boolean debug = true;
    /**
     * FOR USE ONLY BY THE CLIENT. Initialises the connection the server.
     */
    public Connection(){
        String HOSTNAME;
        if (LOCAL) {
            HOSTNAME="localhost";
        }
        else {
            HOSTNAME="46.101.84.55";
        }
        try {
            socket = new Socket(HOSTNAME,PORT);
        } catch (IOException e) {
            try {
                if (!LOCAL) {
                    HOSTNAME="localhost";
                }
                else {
                    HOSTNAME="46.101.84.55";
                }
                socket = new Socket(HOSTNAME,PORT);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        establishConnection();
    }

    /**
     * FOR USE ONLY BY SERVER. Initialises the connection to a client.
     * @param socket The server socket.
     */
    public Connection(Socket socket) {
        this.socket = socket;
        out("Connection made to client.");
        establishConnection();
    }

    /**
     * Creates the input and output streams.
     */
    private void establishConnection(){
        try {
            toConnection = new NetworkSender(new ObjectOutputStream(socket.getOutputStream()));
            fromConnection = new NetworkListener(new ObjectInputStream(socket.getInputStream()), handler);
        } catch (IOException e) {
            out("Failed to establish connection.");
        }
        out("Connection made to server.");
        new Thread(toConnection).start();
        new Thread(fromConnection).start();
        new Thread(handler).start();
    }

    /**
     * Closes all streams.
     */
    private void closeConnection(){
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
     * @return The socket.
     */
    public static ServerSocket getServerSocket(){
        try {
            return new ServerSocket(PORT);
        } catch (IOException e) {
            out("Failed to connect through server socket.");
        }
        return null;
    }

    /**
     * Sends a Sendable object the partner.
     * @param obj Sendable item.
     */
    public void send(Sendable obj){
        toConnection.queueForSending(obj);
    }

    /**
     * This re-initialises the connection in case of an error.
     */
    public void resetConnection(){
        closeConnection();
        establishConnection();
    }

    public void addFunctionEvent(String className, Consumer<Sendable> consumer){
        handler.addFunction(className, consumer);
    }

    /**
     * For debugging use only. Remove all references for production.
     * @param o Object to print.
     */
    static void out(Object o){
        if(debug) System.out.println("[NETWORK] "+o);
    }
}
