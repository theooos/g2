package server.ai.vision;

import server.game.Vector2;
import server.game.Wall;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;

/**
 * Created by rhys on 2/24/17.
 */
public class VisibilityPolygon extends Polygon{

    private ArrayList<Wall> walls;
    private HashSet<Vector2> testVertices;
    private HashSet<Vector2> cornerVertices;
    private final int SIGHT_RANGE = 200;
    private ArrayList<Line2D.Double> TEST_Lines_Of_Sight;

    public VisibilityPolygon(int phase, server.game.Map map){

        super();

        //System.out.println("Polygon created.");


        this.walls = map.wallsInPhase(phase, false, false);
        this.testVertices = new HashSet<>();
        this.cornerVertices = new HashSet<>();
        this.TEST_Lines_Of_Sight = new ArrayList<>();

        for (Wall wall : walls){
            if (wall.isBoundary()) {
                cornerVertices.add(wall.getStartPos());
                cornerVertices.add(wall.getEndPos());
            } else {
                testVertices.add(wall.getStartPos());
                testVertices.add(wall.getEndPos());
            }
        }
    }

    /**
     * Configures this polygon to represent entity's current visibility polygon.
     * @param pov - The entity's position ("Point of View").
     */
    public void visibilityFrom(Vector2 pov){

        // Re-set the polygon.
        reset();
        TEST_Lines_Of_Sight.clear();

        // Sort and clamp the vertices (to restrict the entity's visual range).
        TreeMap<Double, Vector2> sortedVertices = sortVerticesByAngle(pov);

        // Find the breaks in line of sight for each vertex.
        for (java.util.Map.Entry<Double, Vector2> entry : sortedVertices.entrySet()){

            // Draw a line between the pov and a vertex.
            Line2D testLine = new Line2D.Double(pov.toPoint(), entry.getValue().toPoint());

            boolean intersect = false;
            Vector2 closestIntersection = null;

            // Check if it intersected by any of the walls, and if it is, where.
            for (Wall w : walls) {

                Line2D wall = w.toLine();

                if (testLine.intersectsLine(wall)) {

                    double x1 = wall.getX1();
                    double y1 = wall.getY1();
                    double x2 = wall.getX2();
                    double y2 = wall.getY2();

                    double x3 = testLine.getX1();
                    double y3 = testLine.getY1();
                    double x4 = testLine.getX2();
                    double y4 = testLine.getY2();

                    double denominator = (y4 - y3)*(x2 - x1) - (x4 - x3)*(y2 - y1);
                    double ua = ((x4 - x3)*(y1 - y3) - (y4 - y3)*(x1-x3))/denominator;
                    double ub = ((x2 - x1)*(y1 - y3) - (y2 - y1)*(x1 - x3))/denominator;

                    int ix = (int) (x1 + ua*(x2 - x1));
                    int iy = (int) (y1 + ua*(y2 - y1));

                    Vector2 thisIntersection = new Vector2(ix, iy);

                    // Only note this intersection if it is the closest one to the POV so far.
                    if (!intersect || pov.getDistanceTo(thisIntersection) < pov.getDistanceTo(closestIntersection)){
                        closestIntersection = thisIntersection;
                    }

                    intersect = true;
                }
            }

            if (intersect) {
                TEST_Lines_Of_Sight.add(new Line2D.Double(closestIntersection.toPoint(), pov.toPoint()));
                addPoint((int)closestIntersection.getX(), (int)closestIntersection.getY());

            } else {
                TEST_Lines_Of_Sight.add(new Line2D.Double(entry.getValue().toPoint(), pov.toPoint()));
                addPoint((int)entry.getValue().getX(), (int)entry.getValue().getY());
            }
        }
    }

    public ArrayList<Line2D.Double> getTEST_Lines_Of_Sight(){
        return this.TEST_Lines_Of_Sight;
    }


    private TreeMap<Double, Vector2> sortVerticesByAngle(Vector2 pov){

        // Establish the sorting data structure.
        TreeMap<Double, Vector2> sortedVertices = new TreeMap<>();

        // Sort and clamp all the vertices.
        Set<Vector2> union = new HashSet<>(testVertices);
        union.addAll(cornerVertices);
        for (Vector2 v : union) {
            //Vector2 rangedVertex = pov.add(pov.vectorTowards(v).clampedTo(SIGHT_RANGE));
            sortedVertices.put(angleFromNormal(/*rangedVertex*/v, pov), /*rangedVertex*/v);
        }
        return sortedVertices;
    }

    private double angleFromNormal(Vector2 vertex, Vector2 pov) {
        Vector2 normal = pov.add((new Vector2(0, -1)).clampedTo(SIGHT_RANGE));

        /*
        Vector2 vertexVector = pov.mult(pov.vectorTowards(vertex));
        Vector2 normalVector = pov.mult(pov.vectorTowards(normal)); */

        return 180.0 / Math.PI * Math.atan2(pov.getX() - vertex.getX(), vertex.getY() - pov.getY());
    }

}
