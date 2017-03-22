package networking;

import client.Client;

import java.io.ObjectInputStream;

/**
 * Created by theo on 20/03/2017.
 */
public class NetworkListener_Client extends NetworkListener {

    NetworkListener_Client(ObjectInputStream fromConnection, NetworkEventHandler handler, Client client, ReferenceBool running){
        this.client = client;
        this.fromConnection = fromConnection;
        this.handler = handler;
        this.running = running;
    }

    @Override
    void performShutdown() {
        running.value = false;
        client.returnToMainMenu();
    }
}
