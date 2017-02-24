package client.graphics;

import client.ClientLogic.GameData;

import server.game.Player;
import server.game.Vector2;

import java.util.HashMap;


/**
 * Created by Patrick on 2/15/2017.
 * Deals with connecting the player with Rendering.
 */
public class PlayerRenderer {


    public float xPos;
    public float yPos;
    private GameData gd;
    private HashMap<Integer, Player> players;

    private MapRenderer map;

    public PlayerRenderer(GameData gd) {
        this.gd = gd;
        players = gd.getPlayers();
        Player p = players.get(0);
        Vector2 position = p.getPos();
        this.setyPos(position.getY());
        this.setxPos(position.getX());

    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setxPos(float xposition) {
        this.xPos = xposition;
    }

    public void setyPos(float yposition) {
        this.yPos = yposition;
    }
}
