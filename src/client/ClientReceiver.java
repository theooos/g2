package client;

import networking.Connection;
import objects.GameData;
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

    private int playerID;
    private static boolean DEBUG = true;
    private GameData gameData;

    ClientReceiver(Connection connection) {
        connection.addFunctionEvent("String", this::out);
        connection.addFunctionEvent("Player", this::updatedPlayer);
        connection.addFunctionEvent("AIPlayer", this::updatedPlayer);
        connection.addFunctionEvent("Orb", this::updatedOrb);
        connection.addFunctionEvent("Projectile", this::updatedProjectile);
        connection.addFunctionEvent("MoveObject", this::movePlayer);
        connection.addFunctionEvent("DistDropOffProjectile", this::updatedDistProjectile);
        connection.addFunctionEvent("Scoreboard", this::updatedScoreboard);
        connection.addFunctionEvent("PowerUp", this::updatedPowerUp);
    }

    void setGameData(GameData gameData){
        this.gameData = gameData;
    }

    public void out(Object o) {
        if (DEBUG) System.out.println("[CLIENT] " + o);
    }

    public void setID(int id) {
        this.playerID = id;
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
