package testbed;
import server.ai.vision.VisibilityPolygon;
import server.game.MovableEntity;
import server.game.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by rhys on 19/01/17.
 */
public class EnvironmentView extends JPanel implements Observer, Runnable {

    private TestEnvironment env;
    private Thread thread;

    public EnvironmentView(TestEnvironment env){
        super();
        this.env = env;

        addKeyListener(new ControlPanel(env));
        setBackground(Color.white);
        setFocusable(true);
        setDoubleBuffered(true);

        thread = new Thread(this, "Panel");
        thread.start();

    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        int width = env.getMapWidth();
        int height = env.getMapLength();
        VisibilityPolygon vis = env.getOrb().getSight();
        ArrayList<Line2D.Double> los = env.getOrb().getSight().getTESTlinesofSight();
        g2.clearRect(0,0, width, height);



        // Show visibility polygon.

        g2.setColor(Color.PINK);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++) {
                if (vis.contains(x, y)){
                    Rectangle pixel = new Rectangle(x, y, 1, 1);
                    g2.fill(pixel);
                }
            }
        }

        g2.setColor(Color.BLUE);
        for (Line2D.Double line : los) {
            g2.draw(line);
        }
        
        g2.setColor(Color.GREEN);
        Ellipse2D.Double player = entityToSpot(env.getPlayer());
        g2.fill(player);

        g2.setColor(Color.RED);
        Ellipse2D.Double zombie = entityToSpot(env.getOrb());
        g2.fill(zombie);
        
        g2.setColor(Color.BLACK);
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.draw(player);
        g2.draw(zombie);

        ArrayList<Line2D.Double> lines = wallsToLines();
        for (Line2D line : lines){
            g2.draw(line);
        }
        
    }

    public void update(Observable obs, Object obj){

        repaint();
    }

    private Ellipse2D.Double entityToSpot(MovableEntity entity){
        double r = entity.getRadius();
        double xCorner = entity.getPos().getX() - r;
        double yCorner = entity.getPos().getY() - r;

        return new Ellipse2D.Double(xCorner, yCorner, r*2, r*2);
    }

    private ArrayList<Line2D.Double> wallsToLines(){
        ArrayList<Line2D.Double> lines = new ArrayList<>();
        for (Wall wall : env.getWalls()){
            double x1 = wall.getStartPos().getX();
            double y1 = wall.getStartPos().getY();
            double x2 = wall.getEndPos().getX();
            double y2 = wall.getEndPos().getY();
            lines.add(new Line2D.Double(x1, y1, x2, y2));
        }
        return lines;
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
