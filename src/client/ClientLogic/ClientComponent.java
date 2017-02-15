package client.ClientLogic;

import client.graphics.GameRenderer;
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

    //PlayerConnection pconn;
    Connection conn ;

    GameRenderer renderer;

    public ClientComponent(Connection conn){

        super();
        //this.pconn= pconn;
        this.conn = conn;

        ClientSendable cs = new ClientSendable(conn);
        ClientReceiver cr = new ClientReceiver(conn);

    }
}