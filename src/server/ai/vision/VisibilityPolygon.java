package server.ai.vision;

import server.game.Map;
import server.game.Wall;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by rhys on 2/24/17.
 */
public class VisibilityPolygon extends Polygon{

    private ArrayList<Wall> walls;
    private HashSet<Point> testVertices;
    private ArrayList<Line2D.Double> TESTlinesofSight;

    public VisibilityPolygon(int phase, Map map){

        super();

        this.walls = map.wallsInPhase(phase, false);
        this.testVertices = new HashSet<>();
        this.TESTlinesofSight = new ArrayList<>();

        for (Wall wall : walls){
            testVertices.add(wall.getStartPos().toPoint());
            testVertices.add(wall.getEndPos().toPoint());
        }
    }

    /**
     * Configures this polygon to represent entity's current visibility polygon.
     * @param pov - The entity's position ("Point of View").
     */
    public void visibilityFrom(Point pov){
        reset();
        TESTlinesofSight.clear();

        System.out.println("Computing...");
        System.out.println("Orb at " + pov.toString());
        for (Point v : testVertices){

            System.out.println("Point at " + v.toString());
            // Draw a line between the pov and a vertex.
            Line2D testLine = new Line2D.Double(pov, v);

            boolean intersect = false;
            Point closestIntersection = null;

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

                    Point thisIntersection = new Point(ix, iy);

                    // Only note this intersection if it is the closest one to the POV so far.
                    if (!intersect || pov.distance(thisIntersection) < pov.distance(closestIntersection)){
                        closestIntersection = thisIntersection;
                    }

                    intersect = true;
                }
            }

            if (intersect) {
                TESTlinesofSight.add(new Line2D.Double(closestIntersection, pov));
                addPoint((int)closestIntersection.getX(), (int)closestIntersection.getY());

            } else {
                TESTlinesofSight.add(new Line2D.Double(v, pov));
                addPoint((int)v.getX(), (int)v.getY());
            }
        }
    }

    public ArrayList<Line2D.Double> getTESTlinesofSight(){
        return this.TESTlinesofSight;
    }

}
