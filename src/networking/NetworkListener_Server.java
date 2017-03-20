package networking;

import client.Client;

import java.io.ObjectInputStream;

/**
 * Created by theo on 20/03/2017.
 */
public class NetworkListener_Server extends NetworkListener {

    NetworkListener_Server(ObjectInputStream fromConnection, NetworkEventHandler handler){
        this.fromConnection = fromConnection;
        this.handler = handler;
    }

    @Override
    void performShutdown() {

    }
}
