package client.graphics;

import client.ClientLogic.GameData;
import client.graphics.Sprites.ISprite;
import client.graphics.Sprites.OrbTexture;
import client.graphics.Sprites.PlayerTexture;
import networking.Connection;
import objects.MoveObject;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import server.game.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bianca on 07/03/2017.
 */
public class SpriteManager {

    private MapRenderer map;

    public SpriteManager(MapRenderer map){
        this.map = map;
    }

    public void initialiseSprites(int playerID, GameData gameData) {
        int phase = gameData.getPlayer(playerID).getPhase();

        // Players
        ConcurrentHashMap<Integer, Player> players = gameData.getPlayers();
        int radius = players.get(0).getRadius();

        for (Player p : players.values()) {
                PlayerTexture texture;
                PlayerTexture weapon;
                if (p.getTeam() == 0) {
                    texture = new PlayerTexture(ISprite.PLAYER_P1);
                    weapon = new PlayerTexture((ISprite.PLAYER_P1));
                } else {
                    texture = new PlayerTexture(ISprite.PLAYER_P2);
                    weapon = new PlayerTexture(ISprite.PLAYER_P2);
                }
                texture.setDimension(radius, radius);
                texture.spawn(p.getID(), GameRenderer.correctCoordinates(new Vector2f(p.getPos().getX(), p.getPos().getY())), p.getPhase(),GameRenderer.players);
                weapon.setDimension(radius/2,radius/2);
                weapon.spawn(p.getID(), GameRenderer.correctCoordinates(new Vector2f(p.getPos().getX(), p.getPos().getY())), p.getPhase(), GameRenderer.crosshead);
        }
        // Orbs
        for (Orb o : gameData.getOrbs().values()) {
                OrbTexture texture;
                if (phase == 0) {
                    texture = new OrbTexture(ISprite.ORB_P1);
                } else {
                    texture = new OrbTexture(ISprite.ORB_P2);
                }
                texture.spawn(o.getID(), GameRenderer.correctCoordinates(new Vector2f(o.getPos().getX(), o.getPos().getY())), o.getPhase(), GameRenderer.orbs);
        }
    }

    private Vector2 getDirFromMouse(Vector2 pos) {
        Vector2 mousePos = new Vector2(Mouse.getX(), Mouse.getY());
        Vector2 dir = pos.vectorTowards(mousePos);
        dir = dir.normalise();
        return new Vector2(dir.getX(), 0-dir.getY());
    }

    private Vector2 positionBullet(Vector2 pos, Vector2 dir) {
        Vector2 cursor = pos.add((new Vector2(dir.getX(), 0-dir.getY())).mult(21));
        float lastX = cursor.getX();
        float lastY = cursor.getY();

        return new Vector2(lastX,lastY);
    }

    public void updateSprites(int playerID, GameData gameData, Connection conn){
        for (Player p : gameData.getPlayers().values()) {
            GameRenderer.players.update(p.getID(), GameRenderer.correctCoordinates(new Vector2f(p.getPos().getX(), p.getPos().getY())), p.getPhase());

            Vector2 crossPos;
            if(p.getID() != playerID) {
                crossPos = positionBullet(correctVector(new Vector2(p.getPos().getX(), p.getPos().getY())), p.getDir());
            }
            else {
                Vector2 pos = correctVector(new Vector2(p.getPos().getX(), p.getPos().getY()));
                Vector2 dir = getDirFromMouse(pos);
                crossPos = positionBullet(pos, dir);
                p.setDir(dir);
                conn.send(new MoveObject(p.getPos(), p.getDir(), playerID, p.getMoveCount()));
            }
            GameRenderer.crosshead.update(p.getID(), new Vector2f(crossPos.getX(), crossPos.getY()), p.getPhase());
        }
        for (Orb o : gameData.getOrbs().values()) {
            GameRenderer.orbs.update(o.getID(), GameRenderer.correctCoordinates(new Vector2f(o.getPos().getX(), o.getPos().getY())), o.getPhase());
        }
    }

    public void drawProjectiles(int phase, GameData gameData){
        GameRenderer.projectiles.destroy();

        ConcurrentHashMap<Integer, Projectile> projectiles = gameData.getProjectiles();
        for (Projectile p : projectiles.values()) {
            if (phase == p.getPhase()) {
                PlayerTexture texture;
                if (p.getTeam() == 0) {
                    texture = new PlayerTexture(ISprite.PLAYER_P1);
                } else {
                    texture = new PlayerTexture(ISprite.PLAYER_P2);
                }
                texture.setDimension(p.getRadius(), p.getRadius());
                texture.spawn(p.getID(), GameRenderer.correctCoordinates(new Vector2f(p.getPos().getX(), p.getPos().getY())), p.getPhase(),GameRenderer.projectiles);
                //draw.drawAura(p.getPos(),radius+radius/2,radius/2, red, green, blue);
            }
        }
    }

    public void drawCollision(int id, Vector2f vector2f, int radius, int phase){
        PlayerTexture circle = new PlayerTexture(ISprite.COLLISION);
        circle.setDimension(radius,radius);
        circle.spawn(id, vector2f, phase, GameRenderer.collision);
    }

    public Vector2 correctVector(Vector2 oldCoordinates) {
        float newX = oldCoordinates.getX() + map.MAP_BOUND;
        float newY = map.getHeight() - oldCoordinates.getY() + map.MAP_BOUND;
        return new Vector2(newX, newY);
    }
}
