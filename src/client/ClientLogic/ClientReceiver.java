package client.ClientLogic;

import networking.Connection;
import objects.InitGame;
import objects.MoveObject;
import objects.Sendable;
import server.game.*;

import java.util.function.Consumer;


/**
 * Created by Patrick on 2/11/2017.
 * Deals with the objects received from the Server
 */
public class ClientReceiver {

    private Connection connection;
    private Consumer<GameData> beginGame;
    private int playerID;
    private static boolean DEBUG = true;
    private GameData gameData;

    public ClientReceiver(Connection connection, Consumer<GameData> beginGame) {
        this.connection = connection;
        this.beginGame = beginGame;

        connection.addFunctionEvent("String", this::out);
        connection.addFunctionEvent("String", this::getID);
        connection.addFunctionEvent("InitGame", this::setupGame);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", this::updatedPlayer);
        connection.addFunctionEvent("Orb", this::updatedOrb);
        connection.addFunctionEvent("Projectile", this::updatedProjectile);
        connection.addFunctionEvent("MoveObject", this::movePlayer);
        connection.addFunctionEvent("DistDropOffProjectile", this::updatedDistProjectile);
        connection.addFunctionEvent("Scoreboard", this::updatedScoreboard);
        connection.addFunctionEvent("PowerUp", this::updatedPowerUp);
    }

    private void setupGame(Sendable s) {
        InitGame initGame = (InitGame) s;
        gameData = new GameData(initGame);
        beginGame.accept(gameData);
    }

    private void getID(Object o) {
        String information = o.toString();
        String t = information.substring(0, 2);

        switch (t) {
            case "ID":
                out("FUCK! "+t);
                String idS = information.substring(2);
                int id = Integer.parseInt(idS);
                this.setID(id);
                break;
        }
    }

    public void out(Object o) {
        if (DEBUG) System.out.println("[CLIENT] " + o);
    }

    private void setID(int id) {
        this.playerID = id;
    }

    private int getID() {
        return playerID;
    }

    private void updatedPlayer(Sendable s) {
        Player p = (Player) s;
        if (p.getID() != playerID) {
            gameData.updatePlayer(p);
        }
        else {
            gameData.updateMe(p);
        }
    }

    private void updatedOrb(Sendable s) {
        Orb o = (Orb) s;
        gameData.updateOrb(o);

    }

    private void updatedProjectile(Sendable s) {
        Projectile p = (Projectile) s;
        gameData.updateProjectile(p);
    }

    private void updatedPowerUp(Sendable s) {
        PowerUp p = (PowerUp) s;
        gameData.updatePowerUp(p);
    }

    private void updatedScoreboard(Sendable s) {
        Scoreboard sb = (Scoreboard) s;
        out(s.toString());
        gameData.updateScoreboard(sb);
    }

    private void updatedDistProjectile(Sendable s) {
        DistDropOffProjectile p = (DistDropOffProjectile) s;
        gameData.updateProjectile(p);
    }

    private void movePlayer(Sendable s) {
        MoveObject m = (MoveObject) s;
        Player p = gameData.getPlayer(m.getID());
        p.setPos(m.getPos());
        p.setMoveCount(m.getMoveCounter());
        gameData.updatePlayer(p);
    }
}
