package server.game;

import objects.GameData;

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
    private HashMap<Integer, PowerUp> powerUps;

    /**
     * Sets up a new Collision Manager using a game data object
     * Used for the client
     * @param gd The GameData object to initialise the collision manager
     */
    public CollisionManager(GameData gd) {
        players = gd.getPlayers();
        orbs = gd.getOrbs();
        powerUps = gd.getPowerUps();

        map = new Map(gd.getMapID());
    }

    /**
     * Sets up a new CollisionManager
     * @param players the players to check collision with
     * @param orbs the orbs to check collision with
     * @param map the map to check wall collisions
     * @param powerUps the powers to check collision with
     */
    public CollisionManager(ConcurrentHashMap<Integer, Player> players, HashMap<Integer, Orb> orbs, Map map, HashMap<Integer, PowerUp> powerUps) {
        this.players = players;
        this.powerUps = powerUps;
        this.orbs = orbs;
        this.map = map;
    }

    /**
     * Checks to see if the given player collides with a power up
     * @param p the player to check collision with
     * @return the power up which the player has collided with
     *      or null if there was no collision
     */
    PowerUp collidesWithPowerUp(Player p) {
        for (PowerUp pu: powerUps.values()) {
            if (p.getPhase() == pu.getPhase() && pointsCollided(p.getRadius(), p.getPos(), pu.getRadius(), pu.getPos()) && p.isAlive()) {
                return pu;
            }
        }
        return null;
    }

    /**
     * Checks to see if the given entity is in a valid position
     * @param entity The entity to check the position of
     * @return boolean if the position is valid
     */
    public boolean validPosition(MovableEntity entity) {
        return !pointWallCollision(entity.getPos(), entity.getRadius(), entity.getPhase()) && collidesWithPlayerOrBot(entity) == null;
    }

    /**
     * Checks to see if a point is in a valid position
     * @param pos a Vector2 of the centre of the point
     * @param radius the radius of the point
     * @param phase the phase of a point
     * @return boolean if the position is valid
     */
    public boolean validPosition(Vector2 pos, int radius, int phase){
        return !pointWallCollision(pos, radius, phase) && collidesWithPlayerOrBot(radius, pos, phase) == null;
    }

    /**
     * Checks to see if an orb is in a valid position.
     * This ignores players
     * @param o the orb to check the position of
     * @return boolean if the position is valid
     */
    public boolean orbValidPosition(Orb o) {
        return !pointWallCollision(o.getPos(), o.getRadius(), o.getPhase()) && collidesWithBot(o) == null;
    }

    /**
     * Checks to see if a given point collides with a wall
     * @param point the centre of the point
     * @param r the radius of the point
     * @param phase the phase the point is in
     * @return boolean if the position is valid
     */
    boolean pointWallCollision(Vector2 point, int r, int phase) {
        for (Wall w: map.wallsInPhase(phase, false, false)) {
            if (linePointDistance(w.getStartPos(), w.getEndPos(), point) < (r+5)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the distance of a point from a line
     * @param v the start of the line
     * @param w the end of the line
     * @param p the point
     * @return The distance as a float
     */
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
     * Checks to see if the given entity collides with an entity that isn't itself.
     * @param e the movable entity to check collisions with
     * @return the entity if there is a collision, null if there isn't
     */
    MovableEntity collidesWithPlayerOrBot(MovableEntity e) {
        Vector2 nextPos;
        float minDist;

        //steps through every player
        for(Player p: players.values()) {
            if (!(p.equals(e) ||  p.getPhase() != e.getPhase()) && p.getRadius() > 0) {

                //a projectile doesn't want to collide with it's parent
                if (!(e instanceof Projectile) || !((Projectile) e).getPlayer().equals(p)) {
                    //checks to see if the next move collides
                    nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));
                    minDist = linePointDistance(e.getPos(), nextPos, p.getPos());
                    if (minDist < (e.getRadius() + p.getRadius())) {
                        return p;
                    }
                }
            }
        }
        return collidesWithBot(e);
    }

    private MovableEntity collidesWithBot(MovableEntity e) {
        Vector2 nextPos;
        float minDist;

        //checks through every orb
        for(Orb o: orbs.values()) {
            if (!(o.equals(e) ||  o.getPhase() != e.getPhase()) && o.isAlive()) {
                nextPos = e.getPos().add(e.getDir().mult(e.getSpeed()));
                minDist = linePointDistance(e.getPos(), nextPos, o.getPos());
                if (minDist < (e.getRadius() + o.getRadius())) {
                    return o;
                }
            }
        }

        return null;
    }

    /**
     * Given a radius and a position, it checks to see if it collided with a player or bot
     * that is still alive.  Ignores phase
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

    private MovableEntity collidesWithPlayerOrBot(int r, Vector2 pos, int phase) {
        for (Player p : players.values()){
            if (p.getPhase() == phase && p.isAlive() && pointsCollided(r, pos, p.getRadius(), p.getPos())) return p;
        }

        for (Orb o: orbs.values()) {
            if (o.getPhase() == phase && o.isAlive() && pointsCollided(r, pos, o.getRadius(), o.getPos())) return o;
        }

        return null;
    }

    /**
     * Checks to see if a projectile collides with a wall
     * @param p1 point which is the centre of the projectile
     * @param dir the direction of the projectile
     * @param speed the speed of the projectile
     * @param phase the phase the projectile is in
     * @return boolean whether there is a collision or not
     */
    boolean projectileWallCollision(Vector2 p1, Vector2 dir, float speed, int phase) {
        //works out the position next tick
        Vector2 p2 = p1.add(dir.mult(speed));
        //creates a line from the positions
        Line2D l1 = new Line2D.Float(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        //checks if lines intersect
        for (Wall w: map.wallsInPhase(phase, true, false)) {
            Line2D l2 = w.toLine();
            if (l2.intersectsLine(l1)) return true;
        }

        return false;
    }
}
