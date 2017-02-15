package client.ClientLogic;

import networking.Connection;
import objects.String;
import server.game.Player;
import server.game.Vector2;

/**
 * Created by Patrick on 2/11/2017.
 * Deals with the objects sent to the Server
 */


public class ClientSendable {


    private Connection conn;

    /**
     *
     *
     * @param conn the connection to the server
     */
    public ClientSendable(Connection conn)
    {

        this.conn = conn;


    }

    /**
     *
     * method to send that player has fired
     * @param message a firing message
     */
    public void sendFiringAlert(String message)
    {

        conn.send(message);
    }

    /**
     * method to send a string to the server
     *
     * @param m a string to be sent
     */
    public void sendString(String m) {

        conn.send(m);
    }

    /**
     * send the updated position to the server
     * @param v the vector of the new position
     */
    public void sendPosition(Player p)
    {

        conn.send(p);
    }

    /**
     * sedn the player with the updated position and stuff
     * @param p
     */
    public void sendPlayer(Player p)
    {

        conn.send(p);
    }


}



