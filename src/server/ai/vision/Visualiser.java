package server.ai.vision;

import server.game.Map;
import server.game.Orb;
import server.game.Player;
import server.game.Wall;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rhys on 3/12/17.
 */
public class Visualiser {

    private Map map;
    private ConcurrentHashMap<Integer, Player> players;
    private HashMap<Integer, Orb> orbs;
    private int myID;
    private final int SIGHT_RANGE = 500;

    public Visualiser(Map map, ConcurrentHashMap<Integer, Player> players, int id) {
        this.map = map;
        this.players = players;
        this.orbs = null;
        this.myID = id;
    }

    public Visualiser(Map map, ConcurrentHashMap<Integer, Player> players, HashMap<Integer, Orb> orbs, int id) {
        this.map = map;
        this.players = players;
        this.orbs = orbs;
        this.myID = id;
    }

    public ConcurrentHashMap<Integer, Player> getPlayersInSight(Point2D pov, int phase){
        ConcurrentHashMap<Integer, Player> visPlayers = new ConcurrentHashMap<>();
        ArrayList<Line2D> iLines = getIntersectionLines(phase);
        HashMap<Integer, Line2D> pLines = getPlayerTestingLines(pov, phase);

        for (java.util.Map.Entry<Integer, Line2D> p : pLines.entrySet()) {
            boolean intersection = false;
            for (Line2D w : iLines) {
                if (p.getValue().intersectsLine(w)){
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

    public ConcurrentHashMap<Integer, Orb> getOrbsInSight(Point2D pov, int phase){
        ConcurrentHashMap<Integer, Orb> visOrbs = new ConcurrentHashMap<>();
        ArrayList<Line2D> iLines = getIntersectionLines(phase);
        HashMap<Integer, Line2D> oLines = getOrbTestingLines(pov, phase);

        for (java.util.Map.Entry<Integer, Line2D> p : oLines.entrySet()) {
            boolean intersection = false;
            for (Line2D w : iLines) {
                if (p.getValue().intersectsLine(w)){
                    intersection = true;
                    break;
                }
            }
            if (!intersection) {
                int pID = p.getKey();
                visOrbs.put(pID, orbs.get(pID));
            }
        }
        return visOrbs;
    }

    private ArrayList<Line2D> getIntersectionLines(int phase){
        ArrayList<Line2D> iLines = new ArrayList<>();
        for (Wall wall : map.wallsInPhase(phase, true, true)){
            iLines.add(wall.toLine());
        }
        return iLines;
    }

    public HashMap<Integer, Line2D> getPlayerTestingLines(Point2D pov, int phase){
        boolean orbVision = (orbs == null);
        HashMap<Integer, Line2D> pLines = new HashMap<>();
        for (java.util.Map.Entry<Integer, Player> e : players.entrySet()) {
            if (orbVision || e.getValue().getTeam() != players.get(myID).getTeam()){
                if (orbVision || e.getValue().getPhase() == phase) {
                    pLines.put(e.getValue().getID(), new Line2D.Double(pov, e.getValue().getPos().toPoint()));
                }
            }
        }
        return pLines;
    }

    public HashMap<Integer, Line2D> getOrbTestingLines(Point2D pov, int phase){
        HashMap<Integer, Line2D> oLines = new HashMap<>();
        for (java.util.Map.Entry<Integer, Orb> e : orbs.entrySet()) {
            if (e.getValue().getPhase() == phase) {
                oLines.put(e.getValue().getID(), new Line2D.Double(pov, e.getValue().getPos().toPoint()));
            }
        }
        return oLines;
    }

}
