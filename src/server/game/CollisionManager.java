package server.game;

import client.ClientLogic.GameData;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.geom.Line2D.ptSegDist;

/**
 * Created by peran on 3/6/17.
 * Used to handle all collisions
 */
public class CollisionManager {
    private Map map;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;

    public CollisionManager(GameData gd) {
        players = gd.getPlayers();
        orbs = gd.getOrbs();

        try {
            map = new Map(gd.getMapID());
        } catch (IOException e) {
            System.err.println("IOException: Failed to load map in collisions class");
        }
    }

    public CollisionManager(ConcurrentHashMap<Integer, Player> players, HashMap<Integer, Orb> orbs, Map map) {
        this.players = players;
        this.orbs = orbs;
        this.map = map;
    }

    public boolean validPosition(MovableEntity entity) {
        return !pointWallCollision(entity.getRadius(), entity.getPos(), entity.getPhase()) && collidesWithPlayerOrBot(entity) == null;
    }

    public boolean orbValidPosition(Orb o) {
        return !pointWallCollision(o.getRadius(), o.getPos(), o.getPhase()) && collidesWithBot(o) == null;
    }

    boolean pointWallCollision(int r, Vector2 point, int phase) {
        for (Wall w: map.wallsInPhase(phase, false, false)) {
            if (linePointDistance(w.getStartPos(), w.getEndPos(), point) < (r+10)) {
                return true;
            }
        }

        return false;
    }

    private float linePointDistance(Vector2 v, Vector2 w, Vector2 p) {
        return (float) ptSegDist(v.getX(), v.getY(), w.getX(), w.getY(), p.getX(), p.getY());
    }

    /**
     * returns true if the two entity have collided
     * @param r1 the radius of the first entity
     * @param p1 the position of the first entity
     * @param r2 the radius of the second entity
     * @param p2 the position of the second entity
     */
    private boolean pointsCollided(int r1, Vector2 p1, int r2, Vector2 p2) {
        return p1.getDistanceTo(p2) < (r1 + r2);
    }

    /**
     * Checks to see if the entity collides with an entity that isn't itself.
     * @param e the movable entity to check collisions with
     * @return the entity if there is a collision, null if there isn't
     */
    MovableEntity collidesWithPlayerOrBot(MovableEntity e) {
        HashMap<Integer, MovableEntity> entities = new HashMap<>();

        for(Player p: players.values()) {
            if (!(p.equals(e) ||  p.getPhase() != e.getPhase())) {
                if (!(e instanceof Projectile)) {
                    entities.put(p.getID(), p);
                }
                else if (!((Projectile) e).getPlayer().equals(p)){
                    entities.put(p.getID(), p);
                }
            }
        }

        Vector2 nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));

        for (MovableEntity et: entities.values()) {
            float minDist = linePointDistance(e.getPos(), nextPos, et.getPos());
            if (minDist < (e.getRadius() + et.getRadius())) {
                return et;
            }
        }
        return collidesWithBot(e);
    }

    MovableEntity collidesWithBot(MovableEntity e) {
        HashMap<Integer, MovableEntity> entities = new HashMap<>();
        for(Orb o: orbs.values()) {
            if (!(o.equals(e) ||  o.getPhase() != e.getPhase())) {
                entities.put(o.getID(), o);
            }
        }

        Vector2 nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));

        for (MovableEntity et: entities.values()) {
            float minDist = linePointDistance(e.getPos(), nextPos, et.getPos());
            if (minDist < (e.getRadius() + et.getRadius())) {
                return et;
            }
        }
        return null;
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a player or bot
     * that is still alive
     * @param r the radius of the object
     * @param pos the centre of the object
     * @return the player or bot it is collided with.  Null if no collision
     */
    MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos) {
        for (Player p: players.values()) {
            if (p.isAlive() && pointsCollided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        for (Orb o: orbs.values()) {
            if (o.isAlive() && pointsCollided(r, pos, o.getRadius(), o.getPos())) return o;
        }

        return null;
    }

    boolean projectileWallCollision(Vector2 p1, Vector2 dir, float speed, int phase) {
        Vector2 p2 = p1.add(dir.mult(speed));
        Line2D l1 = new Line2D.Float(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        for (Wall w: map.wallsInPhase(phase, true, false)) {
            Line2D l2 = new Line2D.Float(w.getStartPos().getX(), w.getStartPos().getY(), w.getEndPos().getX(), w.getEndPos().getY());
            if (l2.intersectsLine(l1)) return true;
        }

        return false;
    }
}
