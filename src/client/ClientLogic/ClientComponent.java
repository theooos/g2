package client.ClientLogic;

import networking.Connection;
import objects.String;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Patrick on 2/11/2017.
 *
 */
public class ClientComponent extends JPanel {

    PlayerConnection pconn;
    Connection conn ;
    public ClientComponent(Connection conn,PlayerConnection pconn){

        super();
        this.pconn= pconn;
        this.conn = conn;

        ClientSendable cs = new ClientSendable(conn);
        ClientReceiver cr = new ClientReceiver(conn);


        String s = new String("fire");

        // listening to firing alert from mouse and sending a message to server
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {

                cs.sendFiringAlert(s);

            }
        });




    }
}