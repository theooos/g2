package networking;

import objects.Sendable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static networking.Connection.out;

/**
 * Sends objects to the server. Currently no messaging queue, this might be necessary later.
 */
class NetworkSender implements Runnable {

    private ObjectOutputStream toConnection;
    private ReferenceBool running;

    private List<Sendable> toSend = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor.
     * @param toConnection The output stream that data will be sent to.
     * @param running
     */
    NetworkSender(ObjectOutputStream toConnection, ReferenceBool running){
        this.toConnection = toConnection;
        this.running = running;
    }

    public void run(){
        while(running.value){
            if(!toSend.isEmpty()){
                send(toSend.get(0));
                toSend.remove(0);
            }
        }
    }

    /**
     * Sends object to the connection.
     * @param obj Object to be sent.
     */
    private void send(Sendable obj){
        try {
            toConnection.writeUnshared(obj);
            toConnection.flush();
        } catch (IOException e) {
            out("Failed to send "+obj+". Breaking connection.");
            running.value = false;
        }

    }

    public void queueForSending(Sendable obj) {
        toSend.add(obj);
    }
}
