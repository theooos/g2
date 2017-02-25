package networking;

import objects.Sendable;
import server.game.Player;

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
    public boolean isRunning;

    private List<Sendable> toSend = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructor.
     * @param toConnection The output stream that data will be sent to.
     */
    NetworkSender(ObjectOutputStream toConnection){
        this.toConnection = toConnection;
    }

    public void run(){
        isRunning = true;
        while(isRunning){
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
            try{
                System.out.println("[SENT] "+((Player)obj).getPos());
            } catch (Exception e){}

        } catch (IOException e) {
            out("Failed to send "+obj);
        }

    }

    /**
     * Closes the output stream.
     * @throws IOException
     */
    void close() throws IOException{
        isRunning = false;
        toConnection.close();
    }

    public void queueForSending(Sendable obj) {
        toSend.add(obj);
    }
}
