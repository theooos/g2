package client.graphics;

import client.ClientLogic.GameData;
import networking.Connection;

public class GameRendererCreator implements Runnable {
    private GameData gd;
    private Connection connection;
    private int id;

    public GameRendererCreator(GameData gd, Connection connection, int id){
        this.gd = gd;
        this.connection = connection;
        this.id = id;
    }

    @Override
    public void run() {
        //System.err.println("Starting Game Renderer Creator");
        GameRenderer gameRenderer = new GameRenderer(gd, connection, id);
        gameRenderer.run();
    }
}
