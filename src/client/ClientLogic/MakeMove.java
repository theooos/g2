package client.ClientLogic;

import server.game.Vector2;

/**
 * Created by Patrick on 2/11/2017.
 */
public class MakeMove {

    private ClientSendable cs;

    public MakeMove(ClientSendable cs) {

        this.cs = cs;

    }

    /**
     * making a move and sending it to the server
     *
     * @param direction the direction of the move
     * @return true if the move has been successfully
     */
    public boolean movePlayer(Vector2 direction) {

        //cs.sendPosition(direction);
        return true;
    }
}

