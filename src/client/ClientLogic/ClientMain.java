package client.ClientLogic;

import networking.Connection;

/**
 * Created by Patrick on 2/11/2017.
 */
public class ClientMain {


    public static void main(String[] args)
    {

        Connection conn = new Connection();
        PlayerConnection p = new PlayerConnection(conn);
        ClientComponent com = new ClientComponent(conn,p);


    }
}
