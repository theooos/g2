package server.ai.decision;

import server.game.Map;
import server.game.Orb;
import server.game.Player;
import server.game.Wall;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows AI-controlled entities to determine which enemy players and orbs they can see.
 * Created by rhys on 3/12/17.
 */
public class Visualiser {

    private Map map;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private int myID;
    private final int SIGHT_RANGE = 500;

    /**
     * Creates a Visualiser object for use by a Player or Orb.
     *
     * @param map       the map the current game is being played on.
     * @param players   the players involved in the current game.
     * @param orbs      the orbs involved in the current game.
     * @param id        the ID of the Orb this Visualisation object will belong to.
     */
    public Visualiser(Map map, ConcurrentHashMap<Integer, Player> players, HashMap<Integer, Orb> orbs, int id) {
        this.map = map;
        this.players = players;
        this.orbs = orbs;
        this.myID = id;
    }

    /**
     * Determines whether a point 'dest' can be seen from another point 'pov' within
     * the given phase.
     *
     * @param pov   the point of view.
     * @param dest  the destination towards which line of sight will be verified.
     * @param phase the phase within which line of sight will be verified.
     * @return      true if the destination can be seen from the POV.
     */
    public boolean inSight(Point2D pov, Point2D dest, int phase){
        ArrayList<Rectangle2D> walls = getWalls(phase);
        Line2D lineOfSight = new Line2D.Double(pov, dest);
        boolean canSee = true;
        for (Rectangle2D w : walls){
            if (w.intersectsLine(lineOfSight)) {
                canSee = false;
                break;
            }
        }
        return canSee;

    }

    /**
     * Returns the collection of opponent players where the line between the player's position
     * and the POV is not intersected by any walls within the game map.
     *
     * @param pov       the position from which vision is being checked.
     * @param phase     the phase within which vision is being checked.
     * @param orbVision whether or not visibility is being tested from the
     *                  perspective of an Orb.
     * @return          the collection of opponent players in sight.
     */
    public ConcurrentHashMap<Integer, Player> getPlayersInSight(Point2D pov, int phase, boolean orbVision){
        ConcurrentHashMap<Integer, Player> visPlayers = new ConcurrentHashMap<>();
        ArrayList<Rectangle2D> walls = getWalls(phase);
        HashMap<Integer, Line2D> pLines = getPlayerTestingLines(pov, phase, orbVision);

        for (java.util.Map.Entry<Integer, Line2D> p : pLines.entrySet()) {
            boolean intersection = false;
            for (Rectangle2D w : walls) {
                if (p.getValue().intersects(w)){
                    intersection = true;
                    break;
                }
            }
            if (!intersection) {
                int pID = p.getKey();
                visPlayers.put(pID, players.get(pID));
            }
        }
        return visPlayers;
    }

    /**
     * Returns the collection of orbs where the line between the orb's position
     * and the POV is not intersected by any walls within the game map.
     *
     * @param pov   the position from which vision is being checked.
     * @param phase the phase within which vision is being checked.
     * @return      the collection of orbs in sight.
     */
    public ConcurrentHashMap<Integer, Orb> getOrbsInSight(Point2D pov, int phase, float range){
        ConcurrentHashMap<Integer, Orb> visOrbs = new ConcurrentHashMap<>();
        ArrayList<Rectangle2D> walls = getWalls(phase);
        HashMap<Integer, Line2D> oLines = getOrbTestingLines(pov, phase);

        for (java.util.Map.Entry<Integer, Line2D> p : oLines.entrySet()) {
            boolean interested = true;
            double dx = Math.abs(p.getValue().getX2() - p.getValue().getX1());
            double dy = Math.abs(p.getValue().getY2() - p.getValue().getY1());
            if (Math.hypot(dx, dy) > range){
                interested = false;
            }
            else {
                for (Rectangle2D w : walls) {
                    if (p.getValue().intersects(w)){
                        interested = false;
                        break;
                    }
                }
            }

            if (interested) {
                int pID = p.getKey();
                visOrbs.put(pID, orbs.get(pID));
            }
        }
        return visOrbs;
    }

    /**
     * Returns a collection of lines representing all intact walls of the current
     * map, within the given phase.
     *
     * @param phase  the phase whose walls the collection will contain.
     * @return       a list of lines corresponding to positions of relevant walls.
     */
    private ArrayList<Rectangle2D> getWalls(int phase){
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        for (Wall wall : map.wallsInPhase(phase, true, true)){
            walls.add(wall.toRect());
        }
        return walls;
    }

    /**
     * Returns a collection of lines where each line represents a ray cast from
     * the POV towards an opponent player within the given phase.
     *
     * @param pov       the position from which the rays are being cast.
     * @param phase     the phase within which the rays are being cast.
     * @param orbVision whether or not visibility is being tested from the
     *                  perspective of an Orb.
     * @return          a list of rays between the pov and relevant players.
     */
    public HashMap<Integer, Line2D> getPlayerTestingLines(Point2D pov, int phase, boolean orbVision){
        HashMap<Integer, Line2D> pLines = new HashMap<>();
        for (java.util.Map.Entry<Integer, Player> e : players.entrySet()) {
            if ((orbVision || e.getValue().getTeam() != players.get(myID).getTeam()) && e.getValue().isAlive()){
                if (orbVision || e.getValue().getPhase() == phase) {
                    pLines.put(e.getValue().getID(), new Line2D.Double(pov, e.getValue().getPos().toPoint()));
                }
            }
        }
        return pLines;
    }

    /**
     * Returns a collection of lines where each line represents a ray cast from
     * the POV towards an Orb within the given phase.
     *
     * @param pov   the position from which the rays are being cast.
     * @param phase the phase within which the rays are being cast.
     * @return      a list of rays between the pov and relevant orbs.
     */
    public HashMap<Integer, Line2D> getOrbTestingLines(Point2D pov, int phase){
        HashMap<Integer, Line2D> oLines = new HashMap<>();
        for (java.util.Map.Entry<Integer, Orb> e : orbs.entrySet()) {
            if (e.getValue().getPhase() == phase && e.getValue().isAlive()) {
                oLines.put(e.getValue().getID(), new Line2D.Double(pov, e.getValue().getPos().toPoint()));
            }
        }
        return oLines;
    }
}
