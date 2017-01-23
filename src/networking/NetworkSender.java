package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectOutputStream;

import static networking.Connection.out;

/**
 * Sends objects to the server. Currently no messaging queue, this might be necessary later.
 */
class NetworkSender {

    private ObjectOutputStream toConnection;

    /**
     * Constructor.
     * @param toConnection The output stream that data will be sent to.
     */
    NetworkSender(ObjectOutputStream toConnection){
        this.toConnection = toConnection;
    }

    /**
     * Sends object to the connection.
     * @param obj Object to be sent.
     */
    void send(Sendable obj){
        try {
            toConnection.writeObject(obj);
            toConnection.flush();
        } catch (IOException e) {
            out("Failed to send "+obj);
        }

    }

    /**
     * Closes the output stream.
     * @throws IOException
     */
    void close() throws IOException{
        toConnection.close();
    }
}
